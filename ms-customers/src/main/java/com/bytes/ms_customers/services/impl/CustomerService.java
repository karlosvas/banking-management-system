package com.bytes.ms_customers.services.impl;

import org.springframework.lang.NonNull;
import com.bytes.ms_customers.dtos.CustomerResponseDTO;
import com.bytes.ms_customers.dtos.CustomerValidationResponse;
import com.bytes.ms_customers.dtos.LoginRequestDTO;
import com.bytes.ms_customers.dtos.LoginResponseDTO;
import com.bytes.ms_customers.dtos.RegisterRequestDTO;
import com.bytes.ms_customers.dtos.RegisterResponseDTO;

import java.util.UUID;

public interface CustomerService {
    RegisterResponseDTO registerCustomer(@NonNull RegisterRequestDTO dto);
    CustomerResponseDTO getCurrentCustomer(@NonNull String email);
    LoginResponseDTO login(@NonNull LoginRequestDTO request);
    CustomerValidationResponse validateCustomer(@NonNull UUID customerId);
}