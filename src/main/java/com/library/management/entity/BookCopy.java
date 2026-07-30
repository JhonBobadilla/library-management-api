package com.library.management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "book_copies")
public class BookCopy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventory_code", nullable = false, unique = true, length = 100)
    private String inventoryCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookCopyStatus status = BookCopyStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public BookCopy() {
    }

    public BookCopy(String inventoryCode, Book book) {
        this.inventoryCode = inventoryCode;
        this.book = book;
        this.status = BookCopyStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public BookCopyStatus getStatus() {
        return status;
    }

    public void setStatus(BookCopyStatus status) {
        this.status = status;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
