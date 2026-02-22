package com.bytes.ms_accounts.dtos;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.UUID;
import com.bytes.ms_accounts.enums.StatusType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDTO {
    private UUID id;
    private String accountNumber;
    private String customerId;
    private String accountType;
    private Currency currency;
    private BigDecimal  balance;
    private String alias;
    private StatusType status;
    private Instant createdAt;
}