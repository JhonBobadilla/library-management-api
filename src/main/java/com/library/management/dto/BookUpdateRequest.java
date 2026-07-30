package com.library.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Solicitud para actualizar un libro existente")
public record BookUpdateRequest(
        @Schema(description = "T\u00edtulo del libro", example = "Don Quijote de la Mancha", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 200) String title,

        @Schema(description = "C\u00f3digo ISBN \u00fanico del libro", example = "9788420412146", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 20) String isbn,

        @Schema(description = "Edici\u00f3n del libro", example = "Edici\u00f3n conmemorativa")
        @Size(max = 100) String edition,

        @Schema(description = "Fecha de publicaci\u00f3n en formato ISO YYYY-MM-DD", example = "1605-01-16")
        LocalDate publicationDate,

        @Schema(description = "Autor del libro", example = "Miguel de Cervantes", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 150) String author
) {
}
