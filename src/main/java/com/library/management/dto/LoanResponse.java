package com.library.management.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanResponse(
        Long id,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDateTime returnedAt,
        String status,
        UserInfo user,
        BookInfo book,
        BookCopyInfo bookCopy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record UserInfo(Long id, String firstName, String lastName, String email) {
    }

    public record BookInfo(Long id, String title, String isbn, String author) {
    }

    public record BookCopyInfo(Long id, String inventoryCode, String status) {
    }
}
