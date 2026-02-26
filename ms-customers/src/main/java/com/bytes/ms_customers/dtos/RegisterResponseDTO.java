package com.bytes.ms_customers.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import com.bytes.ms_customers.enums.CustomerStatus;
import lombok.Builder;
import lombok.Data;

/**
 * RegisterResponseDTO - Response object after successful user registration
 * 
 * @Schema attributes used:
 * - description: Field documentation
 * - example: Sample response values
 * - accessMode: READ_ONLY for all fields (response-only)
 * - allowableValues: For enum fields
 */
@Data
@Builder
@Schema(description = "Response payload containing newly registered customer information")
public class RegisterResponseDTO {
    
    @Schema(
        description = "Unique identifier assigned to the newly registered customer",
        example = "550e8400-e29b-41d4-a716-446655440000",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private UUID id;
    
    @Schema(
        description = "Customer's DNI (Spanish National ID)",
        example = "12345678A",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private String dni;
    
    @Schema(
        description = "Customer's full name (concatenated first and last name)",
        example = "John Doe",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private String fullName;
    
    @Schema(
        description = "Customer's registered email address",
        example = "john.doe@example.com",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private String email;
    
    @Schema(
        description = "Initial status of the newly registered customer account",
        example = "ACTIVE",
        accessMode = Schema.AccessMode.READ_ONLY,
        allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED", "DELETED"}
    )
    private CustomerStatus status;
    
    @Schema(
        description = "Timestamp when the customer account was created",
        example = "2024-01-15T10:30:00Z",
        accessMode = Schema.AccessMode.READ_ONLY,
        format = "date-time"
    )
    private Instant createdAt;
}