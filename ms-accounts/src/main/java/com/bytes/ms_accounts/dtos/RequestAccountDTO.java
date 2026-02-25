package com.bytes.ms_accounts.dtos;

import java.util.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestAccountDTO {

    @NotBlank
    public String accountType;

    @NotNull
    public Currency currency;

    @NotBlank
    public String alias;

}