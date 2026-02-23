package com.bytes.ms_accounts.controllers;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bytes.ms_accounts.dtos.AccountDTO;
import com.bytes.ms_accounts.dtos.RequestAccountDTO;
import com.bytes.ms_accounts.security.JwtUtils;
import com.bytes.ms_accounts.services.AccountServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountServiceImpl accountService;
    private final JwtUtils jwtUtils;

    public AccountController(AccountServiceImpl accountService, JwtUtils jwtUtils) {
        this.accountService = accountService;
        this.jwtUtils = jwtUtils;
    }

    @PutMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody RequestAccountDTO request, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").substring(7);
        UUID customerId = UUID.fromString(
            jwtUtils.extractClaim(token, claims -> claims.get("customerId", String.class))
        );

        // Si el token no es válido o no contiene el customerId, se devuelve un error 401
        if (customerId == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        AccountDTO account = accountService.createAccount(request, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }
}
