package com.bytes.ms_customers.dtos;

import java.time.Instant;

public record ErrorResponseDTO(
    String error,
    String message,
    Instant timestamp,
    int status
) {}