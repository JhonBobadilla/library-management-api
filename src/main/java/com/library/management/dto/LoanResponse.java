package com.library.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Respuesta con los datos de un pr\u00e9stamo")
public record LoanResponse(
        @Schema(description = "Identificador \u00fanico del pr\u00e9stamo", example = "1") Long id,
        @Schema(description = "Fecha de inicio del pr\u00e9stamo", example = "2026-07-29") LocalDate loanDate,
        @Schema(description = "Fecha l\u00edmite de devoluci\u00f3n", example = "2026-08-05") LocalDate dueDate,
        @Schema(description = "Fecha y hora en que fue devuelto (null si no se ha devuelto)") LocalDateTime returnedAt,
        @Schema(description = "Estado del pr\u00e9stamo: SCHEDULED, ACTIVE, OVERDUE o RETURNED", example = "ACTIVE") String status,
        @Schema(description = "Datos del usuario asociado") UserInfo user,
        @Schema(description = "Datos del libro asociado") BookInfo book,
        @Schema(description = "Datos del ejemplar f\u00edsico prestado") BookCopyInfo bookCopy,
        @Schema(description = "Fecha y hora de creaci\u00f3n") LocalDateTime createdAt,
        @Schema(description = "Fecha y hora de la \u00faltima actualizaci\u00f3n") LocalDateTime updatedAt
) {
    @Schema(description = "Informaci\u00f3n b\u00e1sica del usuario")
    public record UserInfo(
            @Schema(description = "Identificador del usuario", example = "1") Long id,
            @Schema(description = "Nombres del usuario", example = "Ana") String firstName,
            @Schema(description = "Apellidos del usuario", example = "Torres") String lastName,
            @Schema(description = "Correo electr\u00f3nico del usuario", example = "ana.torres@example.com") String email) {
    }

    @Schema(description = "Informaci\u00f3n b\u00e1sica del libro")
    public record BookInfo(
            @Schema(description = "Identificador del libro", example = "1") Long id,
            @Schema(description = "T\u00edtulo del libro", example = "Don Quijote de la Mancha") String title,
            @Schema(description = "C\u00f3digo ISBN del libro", example = "9788420412146") String isbn,
            @Schema(description = "Autor del libro", example = "Miguel de Cervantes") String author) {
    }

    @Schema(description = "Informaci\u00f3n b\u00e1sica del ejemplar f\u00edsico")
    public record BookCopyInfo(
            @Schema(description = "Identificador del ejemplar", example = "1") Long id,
            @Schema(description = "C\u00f3digo de inventario", example = "CPY-1-1-ABC123") String inventoryCode,
            @Schema(description = "Estado del ejemplar: AVAILABLE o LOANED", example = "AVAILABLE") String status) {
    }
}
