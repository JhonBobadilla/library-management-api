package com.library.management.repository;

import com.library.management.entity.Loan;
import com.library.management.entity.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT l FROM Loan l WHERE l.user.id = :userId AND l.status IN :statuses")
    List<Loan> findByUserIdAndStatusIn(@Param("userId") Long userId, @Param("statuses") List<LoanStatus> statuses);

    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.user.id = :userId AND l.status IN :statuses")
    boolean existsByUserIdAndStatusIn(@Param("userId") Long userId, @Param("statuses") List<LoanStatus> statuses);

    @Query("SELECT l FROM Loan l JOIN FETCH l.user JOIN FETCH l.bookCopy bc JOIN FETCH bc.book")
    List<Loan> findAllWithDetails();

    @Query("SELECT l FROM Loan l JOIN FETCH l.user JOIN FETCH l.bookCopy bc JOIN FETCH bc.book WHERE l.user.id = :userId")
    List<Loan> findByUserIdWithDetails(@Param("userId") Long userId);

    @Query("SELECT l FROM Loan l JOIN FETCH l.user JOIN FETCH l.bookCopy bc JOIN FETCH bc.book WHERE bc.book.id = :bookId")
    List<Loan> findByBookIdWithDetails(@Param("bookId") Long bookId);

    @Query("SELECT l FROM Loan l JOIN FETCH l.user JOIN FETCH l.bookCopy bc JOIN FETCH bc.book WHERE l.user.id = :userId AND bc.book.id = :bookId")
    List<Loan> findByUserIdAndBookIdWithDetails(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("SELECT l FROM Loan l JOIN FETCH l.user JOIN FETCH l.bookCopy bc JOIN FETCH bc.book WHERE l.id = :id")
    java.util.Optional<Loan> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT l FROM Loan l WHERE l.user.id = :userId AND l.status IN ('ACTIVE', 'OVERDUE')")
    List<Loan> findOpenLoansByUserId(@Param("userId") Long userId);
}
