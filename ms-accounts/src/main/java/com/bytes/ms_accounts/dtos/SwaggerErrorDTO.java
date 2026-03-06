package com.bytes.ms_accounts.dtos;

import java.time.LocalDateTime;
import java.util.Map;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for exception handling")
public record SwaggerErrorDTO(
        @Schema(description = "Error message", type = "string")
        String message,
        @Schema(description = "Detailed error description", type = "string")
        String description,
        @Schema(description = "Error code", type = "integer")
        int code,
        @Schema(description = "Additional error reasons", type = "map")
        Map<String, String> reasons,
        @Schema(description = "Error timestamp", format = "date-time")
        LocalDateTime timestamp
) {
}