package com.bytes.ms_accounts.dtos;

import java.util.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO for creating a new bank account")
public class AccountRequestDTO {
    @NotBlank
    @Schema(description = "Account type to create (SAVINGS, CHECKING, etc)", example = "SAVINGS", allowableValues = {"CHECKING", "SAVINGS"})
    public String accountType;
    @NotNull
    @Schema(description = "Account currency", example = "USD")
    public Currency currency;
    @NotBlank
    @Schema(description = "Account alias or custom name", minLength = 1, maxLength = 50, example = "My savings account")
    public String alias;
}