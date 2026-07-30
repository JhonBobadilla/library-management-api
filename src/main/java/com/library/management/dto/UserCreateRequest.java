package com.library.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Solicitud para crear un nuevo usuario")
public record UserCreateRequest(
        @Schema(description = "Nombres del usuario", example = "Ana", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 100) String firstName,

        @Schema(description = "Apellidos del usuario", example = "Torres", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(max = 100) String lastName,

        @Schema(description = "Correo electr\u00f3nico \u00fanico del usuario", example = "ana.torres@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Email @Size(max = 150) String email,

        @Schema(description = "Fecha de nacimiento en formato ISO YYYY-MM-DD", example = "1992-03-15", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull LocalDate birthDate
) {
}
