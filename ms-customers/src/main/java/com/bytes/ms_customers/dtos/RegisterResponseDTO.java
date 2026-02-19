package com.bytes.ms_customers.dtos;

import java.time.Instant;
import java.util.UUID;
import com.bytes.ms_customers.enums.CustomerStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponseDTO {
    private UUID id;
    private String dni;
    private String fullName;
    private String email;
    private CustomerStatus status;
    private Instant createdAt;
}