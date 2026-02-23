package com.bytes.ms_customers.services;

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

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public RegisterResponseDTO registerCustomer(RegisterRequestDTO dto) {
        // TODO: Cuando se implemente la seguridad, se debe encriptar la contraseña antes de guardarla
        // dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Obtenemos el cliente a partir del DTO y lo guardamos en la base de datos
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
