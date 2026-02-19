package com.bytes.ms_customers.models;

import java.time.Instant;
import java.util.UUID;
import com.bytes.ms_customers.enums.CustomerRole;
import com.bytes.ms_customers.enums.CustomerStatus;
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
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column
    private String dni;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private CustomerRole role;

    @Column
    private Instant createdAt;

    @Column
    private Instant updatedAt;
}