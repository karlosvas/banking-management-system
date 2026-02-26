package com.bytes.ms_customers.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * LoginResponseDTO - Response object containing authentication token
 * 
 * @Schema attributes used:
 * - description: Field documentation
 * - example: Sample JWT token value
 * - accessMode: READ_ONLY for authentication tokens (response-only)
 */
@Data
@AllArgsConstructor
@Schema(description = "Response payload containing JWT authentication token")
public class LoginResponseDTO {
    
    @Schema(
        description = "JWT authentication token for subsequent API requests",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private String token;
    
}