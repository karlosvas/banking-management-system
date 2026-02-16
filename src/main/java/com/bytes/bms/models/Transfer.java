package com.bytes.bms.models;

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
@Table(name = "transfers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
}

// Transfer
// Campo Tipo Descripción
// id UUID Identificador único
// sourceAccountId UUID Cuenta origen
// destinationAccountNumber String IBAN destino
// amount BigDecimal Monto transferido
// fee BigDecimal Comisión aplicada
// concept String Concepto
// status Enum Estado
// scheduledDate LocalDate Fecha programada (nullable)
// executedAt Instant Fecha de ejecución
// createdAt Instant Fecha de creación
