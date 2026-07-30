package com.library.management.dto;

import java.util.List;

public record AvailableCopiesResponse(
        Long bookId,
        String isbn,
        String title,
        long availableCopies,
        List<BookCopyResponse> copies
) {
}
