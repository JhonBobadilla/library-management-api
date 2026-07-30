package com.library.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de un ejemplar f\u00edsico")
public record BookCopyResponse(
        @Schema(description = "Identificador \u00fanico del ejemplar", example = "1") Long id,
        @Schema(description = "C\u00f3digo de inventario \u00fanico del ejemplar", example = "CPY-1-1-ABC123") String inventoryCode,
        @Schema(description = "Estado del ejemplar: AVAILABLE (disponible) o LOANED (prestado)", example = "AVAILABLE") String status
) {
}
