package com.bytes.ms_customers.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^\\d{8}[A-Z]$", message = "Formato de DNI inválido (ej: 12345678A)")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
        message = "La contraseña debe tener mínimo 8 caracteres, una mayúscula y un número"
    )
    private String password;

    @Pattern(
        regexp = "^\\+?\\d{7,15}$",
        message = "Formato de teléfono inválido (ej: +34123456789)"
    )
    private String phone;

    private String address;
}