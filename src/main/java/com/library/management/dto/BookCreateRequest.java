package com.library.management.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record BookCreateRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 20) String isbn,
        @Size(max = 100) String edition,
        LocalDate publicationDate,
        @NotBlank @Size(max = 150) String author,
        @NotNull @Min(1) @Max(100) Integer numberOfCopies
) {
}
