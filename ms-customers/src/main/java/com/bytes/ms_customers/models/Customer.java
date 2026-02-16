package com.bytes.ms_customers.models;

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
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
}

// Customer
// Campo Tipo Descripción
// id UUID Identificador único
// dni String Documento de identidad (único)
// firstName String Nombre
// lastName String Apellidos
// email String Correo electrónico (único)
// password String Hash de la contraseña
// phone String Teléfono de contacto
// address String Dirección postal
// status Enum Estado del cliente
// role Enum Rol del usuario
// createdAt Instant Fecha de creación
// updatedAt Instant Fecha de actualización
