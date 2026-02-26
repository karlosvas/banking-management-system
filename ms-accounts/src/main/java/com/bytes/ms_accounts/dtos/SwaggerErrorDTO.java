package com.bytes.ms_accounts.dtos;

import java.time.LocalDateTime;
import java.util.Map;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para manejar excepciones")
public record SwaggerErrorDTO(
        @Schema(description = "Mensaje del error", type = "string")
        String message,

        @Schema(description = "Descripción detallada del error", type = "string")
        String description,

        @Schema(description = "Código de error", type = "integer")
        int code,

        @Schema(description = "Razones adicionales del error", type = "map")
        Map<String, String> reasons,

        @Schema(description = "Marca de tiempo del error", format = "date-time")
        LocalDateTime timestamp
) {
}