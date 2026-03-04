package com.bytes.ms_accounts.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import com.bytes.ms_accounts.enums.TransactionStatus;
import com.bytes.ms_accounts.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(nullable = false)
    public UUID accountId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public TransactionType type;

    @Column(nullable = false)
    public BigDecimal amount;

    @Column(nullable = false)
    public BigDecimal balanceAfter;

    @Column(length = 200)
    public String concept;

    @Column
    public String counterpartyAccountNumber;

    @Column(length = 100)
    public String counterpartyName;

    @Column(length = 50)
    public String referenceNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public TransactionStatus status;

    @Column(nullable = false)
    public Instant createdAt;
}