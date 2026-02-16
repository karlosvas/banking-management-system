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
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
}

// Account
// Campo Tipo Descripción
// id UUID Identificador único
// accountNumber String IBAN (único)
// customerId UUID ID del cliente (referencia a ms-customers)
// accountType Enum Tipo de cuenta
// currency String Moneda (EUR, USD)
// balance BigDecimal Saldo actual
// alias String Nombre personalizado
// status Enum Estado de la cuenta
// dailyWithdrawalLimit BigDecimal Límite diario de retiro
// createdAt Instant Fecha de creación
// updatedAt Instant Fecha de actualización
