package com.bytes.ms_accounts.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;
import com.bytes.ms_accounts.enums.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing a bank account")
public record AccountResponseDTO (
    @Schema(description = "Unique account identifier", accessMode = Schema.AccessMode.READ_ONLY)
    UUID id,
    @Schema(description = "Bank account number", minLength = 10, maxLength = 20, example = "1234567890")
    String accountNumber,
    @Schema(description = "ID of the account owner", minLength = 36, maxLength = 36)
    String customerId,
    @Schema(description = "Account type (SAVINGS, CHECKING, etc)", allowableValues = {"CHECKING", "SAVINGS"})
    String accountType,
    @Schema(description = "Account currency", example = "USD")
    Currency currency,
    @Schema(description = "Current account balance", minimum = "0.00", example = "5000.00", accessMode = Schema.AccessMode.READ_ONLY)
    BigDecimal  balance,
    @Schema(description = "Account alias or custom name", minLength = 1, maxLength = 50, example = "My checking account")
    String alias,
    @Schema(description = "Account status", accessMode = Schema.AccessMode.READ_ONLY)
    StatusType status,
    @Schema(description = "Account creation date", accessMode = Schema.AccessMode.READ_ONLY)
    Instant createdAt
){}