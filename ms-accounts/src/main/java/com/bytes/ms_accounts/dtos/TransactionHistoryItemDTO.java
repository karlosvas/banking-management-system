package com.bytes.ms_accounts.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.bytes.ms_accounts.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing one movement in account transaction history")
public record TransactionHistoryItemDTO(
    @Schema(description = "Unique transaction identifier")
    UUID id,

    @Schema(description = "Transaction type", allowableValues = {"DEPOSIT", "WITHDRAWAL", "TRANSFER_IN", "TRANSFER_OUT"})
    TransactionType type,

    @Schema(description = "Signed transaction amount", example = "-150.00")
    BigDecimal amount,

    @Schema(description = "Account balance after the transaction", example = "1350.50")
    BigDecimal balance,

    @Schema(description = "Transaction concept", example = "Pago alquiler enero")
    String concept,

    @Schema(description = "Counterparty account number", example = "ES9121000418450200051332")
    String counterpartyAccount,

    @Schema(description = "Counterparty display name", example = "Maria Lopez Fernandez")
    String counterpartyName,

    @Schema(description = "Transaction timestamp in ISO format", example = "2025-01-20T10:30:00Z")
    Instant timestamp
) {}
