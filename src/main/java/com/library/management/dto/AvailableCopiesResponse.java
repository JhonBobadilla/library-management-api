package com.library.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta con los ejemplares disponibles de un libro")
public record AvailableCopiesResponse(
        @Schema(description = "Identificador del libro", example = "1") Long bookId,
        @Schema(description = "C\u00f3digo ISBN del libro", example = "9788420412146") String isbn,
        @Schema(description = "T\u00edtulo del libro", example = "Don Quijote de la Mancha") String title,
        @Schema(description = "Cantidad de ejemplares disponibles", example = "3") long availableCopies,
        @Schema(description = "Lista de ejemplares disponibles") List<BookCopyResponse> copies
) {
}
