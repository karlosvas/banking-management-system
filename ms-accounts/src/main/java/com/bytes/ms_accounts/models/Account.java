package com.bytes.ms_accounts.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.bytes.ms_accounts.enums.AccountStatus;
import com.bytes.ms_accounts.enums.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column
    public String accountNumber;
    
    @Column
    public UUID customerId;

    @Column
    @Enumerated(EnumType.STRING)
    public AccountType accountType;

    @Column
    public String currency;

    @Column
    public BigDecimal balance;

    @Column
    public String alias;

    @Column
    @Enumerated(EnumType.STRING)
    public AccountStatus status;

    @Column
    public BigDecimal dailyWithdrawalLimit;

    @Column
    public Instant createdAt;

    @Column
    public Instant updatedAt;
}


