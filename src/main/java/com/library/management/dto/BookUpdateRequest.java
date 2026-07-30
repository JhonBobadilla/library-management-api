package com.library.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record BookUpdateRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 20) String isbn,
        @Size(max = 100) String edition,
        LocalDate publicationDate,
        @NotBlank @Size(max = 150) String author
) {
}
