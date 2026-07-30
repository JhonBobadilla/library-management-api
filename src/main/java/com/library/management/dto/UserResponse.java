package com.library.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Respuesta con los datos de un usuario")
public record UserResponse(
        @Schema(description = "Identificador \u00fanico del usuario", example = "1") Long id,
        @Schema(description = "Nombres del usuario", example = "Ana") String firstName,
        @Schema(description = "Apellidos del usuario", example = "Torres") String lastName,
        @Schema(description = "Correo electr\u00f3nico del usuario", example = "ana.torres@example.com") String email,
        @Schema(description = "Fecha de nacimiento", example = "1992-03-15") LocalDate birthDate,
        @Schema(description = "Fecha y hora de creaci\u00f3n") LocalDateTime createdAt,
        @Schema(description = "Fecha y hora de la \u00faltima actualizaci\u00f3n") LocalDateTime updatedAt
) {
}
