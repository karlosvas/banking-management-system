package com.bytes.ms_accounts.dtos;

import java.util.UUID;

public record CustomerValidationResponse(
    UUID customerId,
    boolean exists,
    boolean isActive
) {}