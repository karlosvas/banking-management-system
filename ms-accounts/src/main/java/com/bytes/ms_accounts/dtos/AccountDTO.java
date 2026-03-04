package com.bytes.ms_accounts.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;
import com.bytes.ms_accounts.enums.AccountStatus;
import com.bytes.ms_accounts.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO representing a bank account")
public class AccountDTO {
    @Schema(description = "Unique account identifier", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;
    @Schema(description = "Bank account number", minLength = 10, maxLength = 20, example = "1234567890")
    private String accountNumber;
    @Schema(description = "ID of the account owner", minLength = 36, maxLength = 36)
    private String customerId;
    @Schema(description = "Account type (SAVINGS, CHECKING, etc)", allowableValues = {"CHECKING", "SAVINGS"})
    private AccountType accountType;
    @Schema(description = "Account currency", example = "USD")
    private Currency currency;
    @Schema(description = "Current account balance", minimum = "0.00", example = "5000.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal  balance;
    @Schema(description = "Account alias or custom name", minLength = 1, maxLength = 50, example = "My checking account")
    private String alias;
    @Schema(description = "Account status", accessMode = Schema.AccessMode.READ_ONLY)
    private AccountStatus status;
    @Schema(description = "Account creation date", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;
}