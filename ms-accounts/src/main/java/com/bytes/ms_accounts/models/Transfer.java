package com.bytes.ms_accounts.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.bytes.ms_accounts.enums.TransferStatus;

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
@Table(name = "transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column
    public UUID sourceAccountId;
    
    @Column
    public String destinationAccountNumber;

    @Column
    public BigDecimal amount;

    @Column
    public BigDecimal fee;

    @Column
    public String concept;

    @Enumerated(EnumType.STRING)
    public TransferStatus status;
    @Column(nullable = true)
    public LocalDate scheduledDate;

    @Column
    public String executedAt;

    @Column
    public Instant createdAt;
}
