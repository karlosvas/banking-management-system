package com.bytes.ms_accounts.dtos;

import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing an error response")
public record ErrorResponseDTO(
    @Schema(description = "Error message describing the issue", example = "Customer not found")
    String error,
    @Schema(description = "Detailed error message for debugging purposes", example = "No customer found with ID 12345")
    String message,
    @Schema(description = "Timestamp when the error occurred", example = "2024-06-01T12:00:00Z")
    Instant timestamp,
    @Schema(description = "HTTP status code associated with the error", example = "404")
    int status
) {}