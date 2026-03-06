package com.bytes.ms_accounts.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.bytes.ms_accounts.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing a deposit response")
public record DepositResponseDTO (
    @Schema(description = "Unique transaction identifier", accessMode = Schema.AccessMode.READ_ONLY)
    UUID transactionId,
    @Schema(description = "Transaction type", example = "DEPOSIT", accessMode = Schema.AccessMode.READ_ONLY)
    TransactionType type,
    @Schema(description = "Deposit amount", example = "500.00", accessMode = Schema.AccessMode.READ_ONLY)
    BigDecimal amount,
    @Schema(description = "Account balance before the deposit", example = "1000.00", accessMode = Schema.AccessMode.READ_ONLY)
    BigDecimal balanceBefore,
    @Schema(description = "Account balance after the deposit", example = "1500.00", accessMode = Schema.AccessMode.READ_ONLY)
    BigDecimal balanceAfter,
    @Schema(description = "Concept or description for the deposit", example = "Cash deposit at bank")
    String description,
    @Schema(description = "Timestamp of the deposit transaction", accessMode = Schema.AccessMode.READ_ONLY)
    Instant timestamp
){}
