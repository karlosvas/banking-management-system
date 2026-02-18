package com.bytes.ms_accounts.models;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
}

// Transaction
// Campo Tipo Descripción
// id UUID Identificador único
// accountId UUID Cuenta asociada
// type Enum Tipo de transacción
// amount BigDecimal Monto
// balanceAfter BigDecimal Saldo posterior
// concept String Concepto/descripción
// counterpartyAccountNumber String Cuenta contraparte (si aplica)
// counterpartyName String Nombre contraparte
// referenceNumber String Número de referencia
// status Enum Estado de la transacción
// createdAt Instant Fecha de creación