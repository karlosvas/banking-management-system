package com.bytes.ms_customers.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * RegisterRequestDTO - Request object for user registration
 * 
 * @Schema attributes used:
 * - description: Field documentation
 * - example: Sample values for each field
 * - required: Marks mandatory fields
 * - pattern: Regex pattern for validation documentation
 * - minLength/maxLength: String field length constraints
 * - accessMode: WRITE_ONLY for password (sensitive data)
 */
@Data
@Schema(description = "Request payload for user registration")
public class RegisterRequestDTO {

    @NotBlank(message = "DNI is required")
    @Pattern(regexp = "^\\d{8}[A-Z]$", message = "Invalid DNI format (e.g.: 12345678A)")
    @Schema(
        description = "Spanish National ID (DNI) in format: 8 digits + 1 letter",
        example = "12345678A",
        pattern = "^\\d{8}[A-Z]$"
       
    )
    private String dni;

    @NotBlank(message = "First name is required")
    @Schema(
        description = "User's first name",
        example = "John",
        minLength = 1,
        maxLength = 50
       
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(
        description = "User's last name",
        example = "Doe",
        minLength = 1,
        maxLength = 100
       
    )
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(
        description = "User's email address (must be unique and valid)",
        example = "john.doe@example.com"
       
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
        message = "Password must contain at least 8 characters, one uppercase letter, and one number"
    )
    @Schema(
        description = "User's password (minimum 8 characters, at least 1 uppercase letter and 1 digit) - write-only",
        example = "SecurePass123",
        pattern = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
        accessMode = Schema.AccessMode.WRITE_ONLY,
        minLength = 8
    )
    private String password;

    @Pattern(
        regexp = "^\\+?\\d{7,15}$",
        message = "Invalid phone format (e.g.: +34123456789)"
    )
    @Schema(
        description = "User's phone number (7-15 digits, optional country code with +)",
        example = "+34123456789",
        pattern = "^\\+?\\d{7,15}$",
        minLength = 7,
        maxLength = 15
    )
    private String phone;

    @Schema(
        description = "User's physical address",
        example = "123 Main Street, Madrid, Spain",
        maxLength = 255
    )
    private String address;
}