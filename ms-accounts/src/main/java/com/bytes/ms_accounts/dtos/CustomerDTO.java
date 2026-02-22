package com.bytes.ms_accounts.dtos;

import java.time.Instant;
import java.util.UUID;
import com.bytes.ms_accounts.enums.CustomerStatus;
import lombok.Builder;
import lombok.Data;

// Se han añadido campos adicionales como email, phone, address y status por si es necesario para futuras funcionalidades.
@Data
@Builder
public class CustomerDTO {
    private UUID id;
    private String dni;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private CustomerStatus status;
    private Instant createdAt;
}