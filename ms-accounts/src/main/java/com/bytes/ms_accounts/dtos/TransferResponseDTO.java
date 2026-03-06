package com.bytes.ms_accounts.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.bytes.ms_accounts.enums.TransferStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing a bank transfer")
public record TransferResponseDTO (
    @Schema(description = "Unique transfer identifier", accessMode = Schema.AccessMode.READ_ONLY)
    UUID transferId,
    @Schema(description = "Transfer status", allowableValues = {"PENDING", "COMPLETED", "FAILED", "SCHEDULED", "CANCELLED"})
    TransferStatus status,
    @Schema(description = "Source account ID")
    UUID sourceAccount,
    @Schema(description = "Destination account number or identifier")
    String destinationAccountNumber,
    @Schema(description = "Beneficiary full name when available")
    String beneficiaryName,
    @Schema(description = "Transferred amount", example = "250.00")
    BigDecimal amount,
    @Schema(description = "Transfer concept", example = "Invoice payment")
    String concept,
    @Schema(description = "Applied transfer fee", example = "5.00")
    BigDecimal fee,
    @Schema(description = "Total debited amount (amount + fee)", example = "255.00")
    BigDecimal totalDebit,
    @Schema(description = "Transfer creation timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    Instant timestamp
){}
