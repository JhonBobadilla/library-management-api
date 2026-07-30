package com.library.management.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LoanCreateRequest(
        @NotNull Long userId,
        @NotNull Long bookCopyId,
        @NotNull LocalDate loanDate,
        @NotNull LocalDate dueDate
) {
}
