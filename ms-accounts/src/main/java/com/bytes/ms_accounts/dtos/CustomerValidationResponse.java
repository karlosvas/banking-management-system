package com.bytes.ms_accounts.dtos;

import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Customer validation response")
public record CustomerValidationResponse(
    @Schema(description = "Unique customer identifier")
    UUID customerId,
    @Schema(description = "Indicates if the customer exists")
    boolean exists,
    @Schema(description = "Indicates if the customer is active")
    boolean isActive
) {}