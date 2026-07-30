package com.library.management.repository;

import com.library.management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Query("SELECT COUNT(b) > 0 FROM Book b WHERE b.isbn = :isbn AND b.id <> :id")
    boolean existsByIsbnAndIdNot(@Param("isbn") String isbn, @Param("id") Long id);

    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.bookCopy.book.id = :bookId")
    boolean hasLoans(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(bc) FROM BookCopy bc WHERE bc.book.id = :bookId")
    long countCopiesByBookId(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(bc) FROM BookCopy bc WHERE bc.book.id = :bookId AND bc.status = 'AVAILABLE'")
    long countAvailableCopiesByBookId(@Param("bookId") Long bookId);
}
