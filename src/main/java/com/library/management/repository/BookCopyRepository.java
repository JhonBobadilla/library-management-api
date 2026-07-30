package com.library.management.repository;

import com.library.management.entity.BookCopy;
import com.library.management.entity.BookCopyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    List<BookCopy> findByBookId(Long bookId);

    @Query("SELECT bc FROM BookCopy bc WHERE bc.book.isbn = :isbn AND bc.status = :status")
    List<BookCopy> findByBookIsbnAndStatus(@Param("isbn") String isbn, @Param("status") BookCopyStatus status);

    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.bookCopy.id = :copyId")
    boolean hasLoans(@Param("copyId") Long copyId);

    @Query("SELECT COUNT(bc) > 0 FROM BookCopy bc WHERE bc.book.id = :bookId AND bc.status = 'LOANED'")
    boolean hasLoanedCopies(@Param("bookId") Long bookId);
}
