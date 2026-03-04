package com.bytes.ms_accounts.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing a bank transfer")
public record TransferResponseDTO (
    @Schema(description = "Unique transfer identifier", accessMode = Schema.AccessMode.READ_ONLY)
    UUID id,
    @Schema(description = "Source account ID")
    UUID sourceAccountId,
    @Schema(description = "Destination account IBAN", minLength = 15, maxLength = 34, pattern = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$", example = "ES9121000418450200051332")
    String destinationAccountNumber,
    @Schema(description = "Transfer amount", minimum = "0.01", example = "1000.00")
    BigDecimal amount,
    @Schema(description = "Applied fee", minimum = "0.00", example = "5.00", accessMode = Schema.AccessMode.READ_ONLY)
    BigDecimal fee,
    @Schema(description = "Transfer concept", minLength = 1, maxLength = 200, example = "Monthly rent payment")
    String concept,
    @Schema(description = "Transfer status", allowableValues = {"PENDING", "SCHEDULED", "COMPLETED", "FAILED"})
    String status,
    @Schema(description = "Scheduled execution date")
    LocalDate scheduledDate,
    @Schema(description = "Actual execution date and time", accessMode = Schema.AccessMode.READ_ONLY)
    Instant executedAt,
    @Schema(description = "Transfer creation date", accessMode = Schema.AccessMode.READ_ONLY)
    Instant createdAt
){}
