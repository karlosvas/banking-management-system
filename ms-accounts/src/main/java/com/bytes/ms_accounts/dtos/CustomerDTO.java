package com.bytes.ms_accounts.dtos;

import java.time.Instant;
import java.util.UUID;
import com.bytes.ms_accounts.enums.CustomerStatus;
import lombok.Builder;

@Builder
public record CustomerDTO (
    UUID id,
    String dni,
    String firstName,
    String lastName,
    String email,
    String phone,
    String address,
    CustomerStatus status,
    Instant createdAt
) {}