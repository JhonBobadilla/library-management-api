package com.library.management.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookResponse(
        Long id,
        String title,
        String isbn,
        String edition,
        LocalDate publicationDate,
        String author,
        long totalCopies,
        long availableCopies,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
