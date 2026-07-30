package com.library.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Respuesta con los datos de un libro")
public record BookResponse(
        @Schema(description = "Identificador \u00fanico del libro", example = "1") Long id,
        @Schema(description = "T\u00edtulo del libro", example = "Don Quijote de la Mancha") String title,
        @Schema(description = "C\u00f3digo ISBN del libro", example = "9788420412146") String isbn,
        @Schema(description = "Edici\u00f3n del libro", example = "Primera edici\u00f3n") String edition,
        @Schema(description = "Fecha de publicaci\u00f3n", example = "1605-01-16") LocalDate publicationDate,
        @Schema(description = "Autor del libro", example = "Miguel de Cervantes") String author,
        @Schema(description = "Cantidad total de ejemplares f\u00edsicos", example = "5") long totalCopies,
        @Schema(description = "Cantidad de ejemplares disponibles", example = "3") long availableCopies,
        @Schema(description = "Fecha y hora de creaci\u00f3n") LocalDateTime createdAt,
        @Schema(description = "Fecha y hora de la \u00faltima actualizaci\u00f3n") LocalDateTime updatedAt
) {
}
