package com.bytes.ms_customers.services;

import com.bytes.ms_customers.dtos.LoginRequestDTO;
import com.bytes.ms_customers.dtos.LoginResponseDTO;
import com.bytes.ms_customers.models.Customer;
import com.bytes.ms_customers.repositories.CustomerRepository;
import com.bytes.ms_customers.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException; // <--- IMPORTANTE
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

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