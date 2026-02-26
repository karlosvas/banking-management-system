package com.bytes.ms_customers.dtos;

import java.time.LocalDateTime;
import java.util.Map;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SwaggerErrorDTO - Record for standardized error responses in API exceptions
 * 
 * @Schema attributes used:
 * - description: Field documentation
 * - example: Sample error values
 * - accessMode: READ_ONLY for error response fields
 * - format: Specifies the data format (e.g. date-time)
 */
@Schema(description = "Standardized error response DTO for exception handling in API")
public record SwaggerErrorDTO(
        @Schema(
            description = "Brief error message summarizing what went wrong",
            example = "Validation failed",
            accessMode = Schema.AccessMode.READ_ONLY
        )
        String message,

        @Schema(
            description = "Detailed description of the error condition",
            example = "The provided email address is already registered in the system",
            accessMode = Schema.AccessMode.READ_ONLY
        )
        String description,

        @Schema(
            description = "HTTP status code or application-specific error code",
            example = "400",
            accessMode = Schema.AccessMode.READ_ONLY,
            minimum = "100",
            maximum = "599"
        )
        int code,

        @Schema(
            description = "Additional error details as key-value pairs (field name and error reason)",
            example = "{\"email\": \"Already registered\", \"password\": \"Too weak\"}",
            accessMode = Schema.AccessMode.READ_ONLY
        )
        Map<String, String> reasons,

        @Schema(
            description = "Timestamp when the error occurred (ISO 8601 format)",
            example = "2024-01-15T10:30:00",
            accessMode = Schema.AccessMode.READ_ONLY,
            format = "date-time"
        )
        LocalDateTime timestamp
) {
}