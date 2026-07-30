package com.library.management.service;

import com.library.management.dto.*;
import com.library.management.entity.Book;
import com.library.management.entity.BookCopy;
import com.library.management.entity.BookCopyStatus;
import com.library.management.exception.BusinessRuleException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.BookCopyRepository;
import com.library.management.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private static final String BOOK_NOT_FOUND = "BOOK_NOT_FOUND";
    private static final String ISBN_ALREADY_EXISTS = "ISBN_ALREADY_EXISTS";
    private static final String BOOK_HAS_LOANS = "BOOK_HAS_LOANS";
    private static final String BOOK_HAS_LOANED_COPIES = "BOOK_HAS_LOANED_COPIES";

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    public BookService(BookRepository bookRepository, BookCopyRepository bookCopyRepository) {
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    @Transactional
    public BookResponse create(BookCreateRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new BusinessRuleException(ISBN_ALREADY_EXISTS, "ISBN already exists");
        }
        Book book = new Book(request.title(), request.isbn(), request.edition(), request.publicationDate(), request.author());
        book = bookRepository.save(book);

        List<BookCopy> copies = new ArrayList<>();
        for (int i = 0; i < request.numberOfCopies(); i++) {
            String inventoryCode = generateInventoryCode(book.getId(), i + 1);
            copies.add(new BookCopy(inventoryCode, book));
        }
        bookCopyRepository.saveAll(copies);

        return toResponse(book);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK_NOT_FOUND, "Book", "Book not found with id: " + id));
        return toResponse(book);
    }

    @Transactional(readOnly = true)
    public BookResponse findByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK_NOT_FOUND, "Book", "Book not found with isbn: " + isbn));
        return toResponse(book);
    }

    @Transactional
    public BookResponse update(Long id, BookUpdateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK_NOT_FOUND, "Book", "Book not found with id: " + id));
        if (bookRepository.existsByIsbnAndIdNot(request.isbn(), id)) {
            throw new BusinessRuleException(ISBN_ALREADY_EXISTS, "ISBN already exists");
        }
        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setEdition(request.edition());
        book.setPublicationDate(request.publicationDate());
        book.setAuthor(request.author());
        book = bookRepository.save(book);
        return toResponse(book);
    }

    @Transactional
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK_NOT_FOUND, "Book", "Book not found with id: " + id));
        if (bookRepository.hasLoans(id)) {
            throw new BusinessRuleException(BOOK_HAS_LOANS, "Book has registered loans and cannot be deleted");
        }
        if (bookCopyRepository.hasLoanedCopies(id)) {
            throw new BusinessRuleException(BOOK_HAS_LOANED_COPIES, "Book has loaned copies and cannot be deleted");
        }
        bookRepository.delete(book);
    }

    @Transactional(readOnly = true)
    public AvailableCopiesResponse getAvailableCopiesByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException(BOOK_NOT_FOUND, "Book", "Book not found with isbn: " + isbn));
        List<BookCopy> availableCopies = bookCopyRepository.findByBookIsbnAndStatus(isbn, BookCopyStatus.AVAILABLE);
        List<BookCopyResponse> copyResponses = availableCopies.stream()
                .map(c -> new BookCopyResponse(c.getId(), c.getInventoryCode(), c.getStatus().name()))
                .toList();
        return new AvailableCopiesResponse(book.getId(), book.getIsbn(), book.getTitle(), availableCopies.size(), copyResponses);
    }

    private BookResponse toResponse(Book book) {
        long totalCopies = bookRepository.countCopiesByBookId(book.getId());
        long availableCopies = bookRepository.countAvailableCopiesByBookId(book.getId());
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getEdition(),
                book.getPublicationDate(),
                book.getAuthor(),
                totalCopies,
                availableCopies,
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }

    private String generateInventoryCode(Long bookId, int sequence) {
        String suffix = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "CPY-" + bookId + "-" + sequence + "-" + suffix;
    }
}
