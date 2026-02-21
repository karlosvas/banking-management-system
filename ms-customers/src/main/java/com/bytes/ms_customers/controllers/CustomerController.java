package com.bytes.ms_customers.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bytes.ms_customers.dtos.CustomerDTO;
import com.bytes.ms_customers.dtos.RegisterRequestDTO;
import com.bytes.ms_customers.dtos.RegisterResponseDTO;
import com.bytes.ms_customers.services.CustomerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public RegisterResponseDTO registerCustomer(@Valid @RequestBody RegisterRequestDTO customer) {
        return customerService.registerCustomer(customer);
    }

    @GetMapping("/me")
    public CustomerDTO getCurrentCustomer(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        return customerService.getCurrentCustomer(userDetails.getUsername());
    }
    
}

