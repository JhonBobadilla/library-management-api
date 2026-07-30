package com.library.management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Formato uniforme de respuesta para errores de la API")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        @Schema(description = "Marca de tiempo del error") LocalDateTime timestamp,
        @Schema(description = "C\u00f3digo HTTP de la respuesta", example = "409") int status,
        @Schema(description = "Descripci\u00f3n corta del tipo de error", example = "Conflict") String error,
        @Schema(description = "C\u00f3digo de error del negocio", example = "EMAIL_ALREADY_EXISTS") String code,
        @Schema(description = "Mensaje descriptivo del error", example = "Email already exists") String message,
        @Schema(description = "Ruta de la solicitud que origin\u00f3 el error", example = "/api/v1/users") String path,
        @Schema(description = "Errores de validaci\u00f3n por campo (solo en errores 400)") Map<String, String> validationErrors
) {
}
