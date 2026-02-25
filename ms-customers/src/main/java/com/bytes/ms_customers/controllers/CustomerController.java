package com.bytes.ms_customers.controllers;

import com.bytes.ms_customers.dtos.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.bytes.ms_customers.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerCustomer(@Valid @RequestBody RegisterRequestDTO customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.registerCustomer(customer));
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerDTO> getCurrentCustomer(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return ResponseEntity.ok(customerService.getCurrentCustomer(userDetails.getUsername()));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(customerService.login(request));
    }
    @GetMapping("/{customerId}/validate")
    public ResponseEntity<CustomerValidationResponse> validateCustomer(
            @PathVariable UUID customerId,
            @RequestHeader(value = "X-Internal-Service", required = false) String internalService) {

        if (!"ms-accounts".equals(internalService)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso restringido a llamadas internas");
        }

        return ResponseEntity.ok(customerService.validateCustomer(customerId));
    }

}

