package com.bytes.ms_customers.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

/**
 * CustomerValidationResponse - DTO for customer validation results
 *
 * @Schema attributes used:
 * - description: Field documentation in the API spec
 * - example: Sample value shown in Swagger UI
 * - accessMode: READ_ONLY for response-only fields
 */
@Schema(description = "Response containing customer validation information")
public record CustomerValidationResponse (
    
    @Schema(
        description = "Unique identifier of the customer being validated",
        example = "550e8400-e29b-41d4-a716-446655440000",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    UUID customerId,
    
    @Schema(
        description = "Whether the customer exists in the system",
        example = "true",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    boolean exists,
    
    @Schema(
        description = "Whether the customer account is currently active",
        example = "true",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    boolean isActive

){}