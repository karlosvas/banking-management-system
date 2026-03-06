package com.bytes.ms_customers.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * LoginRequestDTO - Request object for user authentication
 * 
 * @Schema attributes used:
 * - description: Field documentation
 * - example: Sample value for the field
 * - required: Marks mandatory fields (in JSON schema)
 * - pattern: For regex validation documentation
 * - accessMode: WRITE_ONLY for password fields (sensitive data)
 */
@Data
@Schema(description = "Request payload for user login")
public class LoginRequestDTO {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email format must be valid (e.g.: user@domain.com)")
    @Schema(
        description = "User's email address used for login",
        example = "john.doe@example.com"
       
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(
        description = "User's password (sensitive - write-only field)",
        example = "SecurePass123",
        accessMode = Schema.AccessMode.WRITE_ONLY,
        minLength = 8
    )
    private String password;

}
