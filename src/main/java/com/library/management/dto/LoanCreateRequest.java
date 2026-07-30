package com.library.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Solicitud para registrar un nuevo pr\u00e9stamo")
public record LoanCreateRequest(
        @Schema(description = "Identificador del usuario que solicita el pr\u00e9stamo", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Long userId,

        @Schema(description = "Identificador del ejemplar f\u00edsico que ser\u00e1 prestado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Long bookCopyId,

        @Schema(description = "Fecha de inicio del pr\u00e9stamo en formato ISO YYYY-MM-DD", example = "2026-07-29", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull LocalDate loanDate,

        @Schema(description = "Fecha l\u00edmite de devoluci\u00f3n en formato ISO YYYY-MM-DD", example = "2026-08-05", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull LocalDate dueDate
) {
}
