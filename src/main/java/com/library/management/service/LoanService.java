package com.library.management.service;

import com.library.management.config.TimeProvider;
import com.library.management.dto.LoanCreateRequest;
import com.library.management.dto.LoanResponse;
import com.library.management.entity.*;
import com.library.management.exception.BusinessRuleException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.BookCopyRepository;
import com.library.management.repository.LoanRepository;
import com.library.management.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    private static final String BOOK_COPY_NOT_FOUND = "BOOK_COPY_NOT_FOUND";
    private static final String BOOK_COPY_NOT_AVAILABLE = "BOOK_COPY_NOT_AVAILABLE";
    private static final String USER_ALREADY_HAS_OPEN_LOAN = "USER_ALREADY_HAS_OPEN_LOAN";
    private static final String LOAN_NOT_FOUND = "LOAN_NOT_FOUND";
    private static final String LOAN_ALREADY_RETURNED = "LOAN_ALREADY_RETURNED";

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookCopyRepository bookCopyRepository;
    private final TimeProvider timeProvider;

    public LoanService(LoanRepository loanRepository, UserRepository userRepository,
                       BookCopyRepository bookCopyRepository, TimeProvider timeProvider) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.timeProvider = timeProvider;
    }

    @Transactional
    public LoanResponse create(LoanCreateRequest request) {
        var today = timeProvider.today();

        if (request.loanDate().isAfter(request.dueDate())) {
            throw new BusinessRuleException("INVALID_DATES", "Due date cannot be before loan date");
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND, "User", "User not found with id: " + request.userId()));

        BookCopy bookCopy = bookCopyRepository.findById(request.bookCopyId())
                .orElseThrow(() -> new ResourceNotFoundException(BOOK_COPY_NOT_FOUND, "BookCopy", "Book copy not found with id: " + request.bookCopyId()));

        updateOpenLoanStatuses(user.getId(), today);

        boolean hasOpenLoan = loanRepository.existsByUserIdAndStatusIn(
                user.getId(), List.of(LoanStatus.SCHEDULED, LoanStatus.ACTIVE, LoanStatus.OVERDUE));
        if (hasOpenLoan) {
            throw new BusinessRuleException(USER_ALREADY_HAS_OPEN_LOAN, "The user already has an open loan.");
        }

        if (bookCopy.getStatus() != BookCopyStatus.AVAILABLE) {
            throw new BusinessRuleException(BOOK_COPY_NOT_AVAILABLE, "Book copy is not available for loan");
        }

        Loan loan = new Loan(request.loanDate(), request.dueDate(), null, user, bookCopy);
        loan.calculateStatus(today);

        bookCopy.setStatus(BookCopyStatus.LOANED);

        loan = loanRepository.save(loan);
        return toResponse(loan);
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> findAll(Long userId, Long bookId) {
        List<Loan> loans;
        if (userId != null && bookId != null) {
            loans = loanRepository.findByUserIdAndBookIdWithDetails(userId, bookId);
        } else if (userId != null) {
            loans = loanRepository.findByUserIdWithDetails(userId);
        } else if (bookId != null) {
            loans = loanRepository.findByBookIdWithDetails(bookId);
        } else {
            loans = loanRepository.findAllWithDetails();
        }

        var today = timeProvider.today();
        for (Loan loan : loans) {
            if (loan.getStatus() != LoanStatus.RETURNED) {
                var newStatus = calculateStatus(loan, today);
                if (newStatus != loan.getStatus()) {
                    loan.setStatus(newStatus);
                    loanRepository.save(loan);
                }
            }
        }

        return loans.stream().map(this::toResponse).toList();
    }

    @Transactional
    public LoanResponse returnBook(Long loanId) {
        Loan loan = loanRepository.findByIdWithDetails(loanId)
                .orElseThrow(() -> new ResourceNotFoundException(LOAN_NOT_FOUND, "Loan", "Loan not found with id: " + loanId));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new BusinessRuleException(LOAN_ALREADY_RETURNED, "Loan has already been returned");
        }

        loan.setReturnedAt(timeProvider.now());
        loan.setStatus(LoanStatus.RETURNED);

        BookCopy bookCopy = loan.getBookCopy();
        bookCopy.setStatus(BookCopyStatus.AVAILABLE);

        loan = loanRepository.save(loan);
        return toResponse(loan);
    }

    private void updateOpenLoanStatuses(Long userId, LocalDate today) {
        List<Loan> openLoans = loanRepository.findOpenLoansByUserId(userId);
        for (Loan loan : openLoans) {
            LoanStatus newStatus = calculateStatus(loan, today);
            if (newStatus != loan.getStatus()) {
                loan.setStatus(newStatus);
                loanRepository.save(loan);
            }
        }
    }

    private LoanStatus calculateStatus(Loan loan, LocalDate today) {
        if (loan.getReturnedAt() != null) {
            return LoanStatus.RETURNED;
        }
        if (today.isBefore(loan.getLoanDate())) {
            return LoanStatus.SCHEDULED;
        }
        if (today.isAfter(loan.getDueDate())) {
            return LoanStatus.OVERDUE;
        }
        return LoanStatus.ACTIVE;
    }

    private LoanResponse toResponse(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnedAt(),
                loan.getStatus().name(),
                new LoanResponse.UserInfo(
                        loan.getUser().getId(),
                        loan.getUser().getFirstName(),
                        loan.getUser().getLastName(),
                        loan.getUser().getEmail()
                ),
                new LoanResponse.BookInfo(
                        loan.getBookCopy().getBook().getId(),
                        loan.getBookCopy().getBook().getTitle(),
                        loan.getBookCopy().getBook().getIsbn(),
                        loan.getBookCopy().getBook().getAuthor()
                ),
                new LoanResponse.BookCopyInfo(
                        loan.getBookCopy().getId(),
                        loan.getBookCopy().getInventoryCode(),
                        loan.getBookCopy().getStatus().name()
                ),
                loan.getCreatedAt(),
                loan.getUpdatedAt()
        );
    }
}
