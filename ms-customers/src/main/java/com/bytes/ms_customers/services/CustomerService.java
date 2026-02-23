package com.bytes.ms_customers.services;

import org.springframework.security.crypto.password.PasswordEncoder; // <--- 1. IMPORTANTE
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.bytes.ms_customers.dtos.CustomerDTO;
import com.bytes.ms_customers.dtos.RegisterRequestDTO;
import com.bytes.ms_customers.dtos.RegisterResponseDTO;
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


    public CustomerService(CustomerRepository customerRepository,
                           CustomerMapper customerMapper,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.passwordEncoder = passwordEncoder;
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
}