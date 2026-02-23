package com.bytes.ms_customers.dtos;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Data
public class LoginRequestDTO {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato debe ser válido (ej: usuario@dominio.com)")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
