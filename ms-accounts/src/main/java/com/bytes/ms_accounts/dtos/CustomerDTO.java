package com.bytes.ms_accounts.dtos;

import java.time.Instant;
import java.util.UUID;

import com.bytes.ms_accounts.enums.CustomerRole;
import com.bytes.ms_accounts.enums.CustomerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO representing customer information")
public class CustomerDTO {
    @Schema(description = "Unique customer identifier", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;
    @Schema(description = "Customer identification document", minLength = 8, maxLength = 20, pattern = "[0-9]{8}[A-Z]", example = "12345678A")
    private String dni;
    @Schema(description = "Customer first name", minLength = 2, maxLength = 50, example = "John")
    private String firstName;
    @Schema(description = "Customer last name", minLength = 2, maxLength = 50, example = "Doe")
    private String lastName;
    @Schema(description = "Customer email address", pattern = "^[A-Za-z0-9+_.-]+@(.+)$", example = "john.doe@example.com")
    private String email;
    @Schema(description = "Customer phone number", minLength = 9, maxLength = 15, pattern = "^[+]?[0-9]{9,15}$", example = "+34612345678")
    private String phone;
    @Schema(description = "Customer address", minLength = 5, maxLength = 100, example = "123 Main St, Madrid")
    private String address;
    @Schema(description = "Customer status", accessMode = Schema.AccessMode.READ_ONLY)
    private CustomerStatus status;
    @Schema(description = "Customer role", accessMode = Schema.AccessMode.READ_ONLY)
    private CustomerRole role;
    @Schema(description = "Customer record creation date", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;
}