package com.library.management.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        LocalDate birthDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
