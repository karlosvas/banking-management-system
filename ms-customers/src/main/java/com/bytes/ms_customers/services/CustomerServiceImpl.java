package com.bytes.ms_customers.services;

import com.bytes.ms_customers.security.JwtUtils;
import com.bytes.ms_customers.services.impl.CustomerService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import com.bytes.ms_customers.dtos.CustomerResponseDTO;
import com.bytes.ms_customers.dtos.CustomerValidationResponse;
import com.bytes.ms_customers.dtos.LoginRequestDTO;
import com.bytes.ms_customers.dtos.LoginResponseDTO;
import com.bytes.ms_customers.dtos.RegisterRequestDTO;
import com.bytes.ms_customers.dtos.RegisterResponseDTO;
import com.bytes.ms_customers.enums.CustomerStatus;
import com.bytes.ms_customers.exceptions.ResourceAlreadyExistsException;
import com.bytes.ms_customers.exceptions.ResourceNotFoundException;
import com.bytes.ms_customers.mappers.CustomerMapper;
import com.bytes.ms_customers.models.Customer;
import com.bytes.ms_customers.repositories.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                           CustomerMapper customerMapper,
                           PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public RegisterResponseDTO registerCustomer(@NonNull RegisterRequestDTO dto) {

        if (customerRepository.existsByEmail(dto.getEmail()))
            throw new ResourceAlreadyExistsException("Cliente", "email", dto.getEmail());

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        Customer mapped = Objects.requireNonNull(
            customerMapper.toCustomer(dto, CustomerStatus.ACTIVE),
            "CustomerMapper returned null"
        );
        Customer saved = customerRepository.save(mapped);

        return customerMapper.toRegisterResponseDTO(saved);
    }

    public CustomerResponseDTO getCurrentCustomer(@NonNull String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);

        if(!customer.isPresent())
            throw new ResourceNotFoundException("Cliente", email);

        return customerMapper.toCustomerResponseDTO(customer.get());
    }
    
    public LoginResponseDTO login(@NonNull LoginRequestDTO request) {
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

    public CustomerResponseDTO getCustomerById(UUID customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (!customer.isPresent())
            throw new ResourceNotFoundException("Cliente", customerId.toString());

        return customerMapper.toCustomerResponseDTO(customer.get());
    }

    public CustomerValidationResponse validateCustomer(@NonNull UUID customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> new CustomerValidationResponse(
                        customer.getId(),
                        true,
                        customer.getStatus() == CustomerStatus.ACTIVE)
                )
                .orElse(new CustomerValidationResponse(
                        customerId,
                        false,
                        false));
    }

}