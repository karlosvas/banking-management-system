package com.bytes.ms_accounts.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.bytes.ms_accounts.enums.TransactionStatus;
import com.bytes.ms_accounts.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing a bank transaction")
public record TransactionDTO (
    @Schema(description = "Unique transaction identifier", accessMode = Schema.AccessMode.READ_ONLY)
    UUID id,
    @Schema(description = "Associated account ID")
    UUID accountId,
    @Schema(description = "Transaction type", allowableValues = {"DEPOSIT", "WITHDRAWAL", "TRANSFER", "PAYMENT"}, example = "DEPOSIT")
    TransactionType type,
    @Schema(description = "Transaction amount", minimum = "0.01", example = "1500.50")
    BigDecimal amount,
    @Schema(description = "Account balance after transaction", minimum = "0.00", example = "5000.00", accessMode = Schema.AccessMode.READ_ONLY)
    BigDecimal balanceAfter,
    @Schema(description = "Transaction concept or description", minLength = 1, maxLength = 200, example = "Monthly salary")
    String concept,
    @Schema(description = "Counterparty account number", pattern = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$", example = "ES9121000418450200051332")
    String counterpartyAccountNumber,
    @Schema(description = "Counterparty name", minLength = 1, maxLength = 100, example = "John Doe")
     String counterpartyName,
    @Schema(description = "Transaction reference number", minLength = 1, maxLength = 50, pattern = "^[A-Z0-9-]+$", example = "REF-2024-001")
    String referenceNumber,
    @Schema(description = "Transaction status", allowableValues = {"PENDING", "COMPLETED", "FAILED"})
    TransactionStatus status,
    @Schema(description = "Transaction creation date", accessMode = Schema.AccessMode.READ_ONLY)
    Instant createdAt
){}
