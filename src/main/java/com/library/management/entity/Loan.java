package com.library.management.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
public class Loan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoanStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_copy_id", nullable = false)
    private BookCopy bookCopy;

    public Loan() {
    }

    public Loan(LocalDate loanDate, LocalDate dueDate, LoanStatus status, User user, BookCopy bookCopy) {
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.status = status;
        this.user = user;
        this.bookCopy = bookCopy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public void calculateStatus(LocalDate today) {
        if (returnedAt != null) {
            this.status = LoanStatus.RETURNED;
        } else if (today.isBefore(loanDate)) {
            this.status = LoanStatus.SCHEDULED;
        } else if (today.isAfter(dueDate)) {
            this.status = LoanStatus.OVERDUE;
        } else {
            this.status = LoanStatus.ACTIVE;
        }
    }
}
