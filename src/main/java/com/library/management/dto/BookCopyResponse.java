package com.library.management.dto;

public record BookCopyResponse(
        Long id,
        String inventoryCode,
        String status
) {
}
