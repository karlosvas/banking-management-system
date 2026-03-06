package com.bytes.ms_customers.controllers;

import com.bytes.ms_customers.anotations.SwaggerApiResponses;
import com.bytes.ms_customers.dtos.CustomerResponseDTO;
import com.bytes.ms_customers.dtos.CustomerValidationResponse;
import com.bytes.ms_customers.dtos.LoginRequestDTO;
import com.bytes.ms_customers.dtos.LoginResponseDTO;
import com.bytes.ms_customers.dtos.RegisterRequestDTO;
import com.bytes.ms_customers.dtos.RegisterResponseDTO;
import com.bytes.ms_customers.exceptions.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bytes.ms_customers.services.CustomerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

@SwaggerApiResponses
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Register a new customer, creates a new customer account with the provided information")
    @ApiResponse(responseCode = "201", description = "Customer registered successfully")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerCustomer(@Valid @RequestBody RegisterRequestDTO customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.registerCustomer(customer));
    }

    @Operation(summary = "Obtain current customer data, returns the authenticated customer's information based on the JWT token provided in the request")
    @ApiResponse(responseCode = "200", description = "Current customer data obtained successfully")
    @GetMapping("/me")
    public ResponseEntity<CustomerResponseDTO> getCurrentCustomer(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(customerService.getCurrentCustomer(userDetails.getUsername()));
    }

    @Operation(
        summary = "Get customer by ID",
        description = "Retrieves a customer's information by their unique identifier (UUID)"
    )
    @ApiResponse(responseCode = "200", description = "Customer retrieved successfully")
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable UUID customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }
    
    @Operation(summary = "Login a customer and obtain JWT token, returns a JWT token if credentials are valid")
    @ApiResponse(responseCode = "200", description = "Customer logged in successfully")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(customerService.login(request));
    }

    @Operation(summary = "Validate a customer by ID (only for internal calls), checks if customer exists and is active")
    @ApiResponse(responseCode = "200", description = "Customer validation performed successfully")
    @GetMapping("/{customerId}/validate")
    public ResponseEntity<CustomerValidationResponse> validateCustomer(
            @PathVariable UUID customerId,
            @RequestHeader(value = "X-Internal-Service", required = false) String internalService) {

        if (!"ms-accounts".equals(internalService))
            throw new ForbiddenException("Acceso restringido a llamadas internas");

        return ResponseEntity.ok(customerService.validateCustomer(customerId));
    }

}

