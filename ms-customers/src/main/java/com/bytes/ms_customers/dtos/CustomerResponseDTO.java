package com.bytes.ms_customers.dtos;

import java.util.UUID;
import com.bytes.ms_customers.enums.CustomerStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * CustomerDTO - Data Transfer Object for Customer entity
 * 
 * Most relevant @Schema attributes:
 * - description: Explains what the field represents
 * - example: Shows a sample value in the API documentation
 * - allowableValues: Lists possible values for the field
 * - required: Indicates if the field is mandatory (note: use @NotNull/@NotBlank for validation)
 * - minimum/maximum: For numeric fields to define valid range
 * - minLength/maxLength: For String fields to define valid length
 * - pattern: Regex pattern validation (e.g., for IBANs, phone numbers)
 * - defaultValue: Default value if not provided
 * - hidden: Exclude field from documentation
 * - accessMode: READ_ONLY for fields like createdAt, WRITE_ONLY for password
 */
@Schema(description = "Data Transfer Object representing a customer")
public record CustomerResponseDTO (

    @Schema(
        description = "Unique customer identifier",
        example = "550e8400-e29b-41d4-a716-446655440000",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    UUID id,
    
    @Schema(
        description = "Customer's DNI (Spanish National ID)",
        example = "12345678A",
        pattern = "^\\d{8}[A-Z]$"
    )
    String dni,

    @Schema(
        description = "Customer's first name",
        example = "John",
        minLength = 1,
        maxLength = 50
       
    )
    String firstName,
    
    @Schema(
        description = "Customer's last name",
        example = "Doe",
        minLength = 1,
        maxLength = 100
       
    )
    String lastName,
    
    @Schema(
        description = "Customer's email address",
        example = "john.doe@example.com"
       
    )
    String email,
    
    @Schema(
        description = "Customer's phone number",
        example = "+34123456789",
        pattern = "^\\+?\\d{7,15}$"
    )
    String phone,
    
    @Schema(
        description = "Customer's physical address",
        example = "123 Main Street, Madrid, Spain",
        maxLength = 255
    )
    String address,
    
    @Schema(
        description = "Customer's current status",
        example = "ACTIVE",
        allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED", "DELETED"}
    )
    CustomerStatus status,
    
    @Schema(
        description = "Timestamp when the customer was created",
        example = "2024-01-15T10:30:00Z",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    String createdAt
){}