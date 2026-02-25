package com.bytes.ms_customers.services;

import com.bytes.ms_customers.dtos.*;
import com.bytes.ms_customers.security.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder; // <--- 1. IMPORTANTE
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.bytes.ms_customers.enums.CustomerStatus;
import com.bytes.ms_customers.exceptions.ResourceNotFoundException;
import com.bytes.ms_customers.mappers.CustomerMapper;
import com.bytes.ms_customers.models.Customer;
import com.bytes.ms_customers.repositories.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;



    public CustomerService(CustomerRepository customerRepository,
                           CustomerMapper customerMapper,
                           PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public RegisterResponseDTO registerCustomer(RegisterRequestDTO dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        Customer saved = customerRepository.save(
                customerMapper.toCustomer(dto, CustomerStatus.ACTIVE)
        );

        return customerMapper.toRegisterResponse(saved);
    }

    public CustomerDTO getCurrentCustomer(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);

        if(!customer.isPresent())
            throw new ResourceNotFoundException("Cliente", email);

        return customerMapper.toCustomerDTO(customer.get());
    }
    public LoginResponseDTO login(LoginRequestDTO request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new BadCredentialsException("Email o contraseña incorrectos");
        }


        String token = jwtUtils.generateToken(
                customer.getEmail(),
                customer.getId(),
                customer.getRole()
        );

        return new LoginResponseDTO(token);
    }



}