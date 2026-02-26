package com.bytes.ms_accounts.controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bytes.ms_accounts.annotations.SwaggerApiResponses;
import com.bytes.ms_accounts.dtos.AccountDTO;
import com.bytes.ms_accounts.dtos.RequestAccountDTO;
import com.bytes.ms_accounts.security.JwtUtils;
import com.bytes.ms_accounts.services.AccountServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@SwaggerApiResponses
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountServiceImpl accountService;
    private final JwtUtils jwtUtils;

    public AccountController(AccountServiceImpl accountService, JwtUtils jwtUtils) {
        this.accountService = accountService;
        this.jwtUtils = jwtUtils;
    }

    @Operation(
        summary = "Create a new account",
        description = "Creates a new bank account for the authenticated customer"
    )
    @ApiResponse(responseCode = "201", description = "Account created successfully")
    @PutMapping
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody RequestAccountDTO request, HttpServletRequest httpRequest) {
        UUID customerId = jwtUtils.getCustomerIdFromRequest(httpRequest);
        AccountDTO account = accountService.createAccount(request, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @Operation(
        summary = "Get all accounts",
        description = "Retrieves all bank accounts for the authenticated customer"
    )
    @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully")
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccount(HttpServletRequest httpRequest) {
        UUID customerId = jwtUtils.getCustomerIdFromRequest(httpRequest);
        List<AccountDTO> listAccounts = accountService.getAccounts(customerId);
        return ResponseEntity.ok().body(listAccounts);
    }

    @Operation(
        summary = "Get account by ID",
        description = "Retrieves a specific bank account by its ID for the authenticated customer"
    )
    @ApiResponse(responseCode = "200", description = "Account retrieved successfully")
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable UUID accountId, HttpServletRequest httpRequest) {
        UUID customerId = jwtUtils.getCustomerIdFromRequest(httpRequest);
        AccountDTO account = accountService.getAccountById(accountId, customerId);
        return ResponseEntity.ok().body(account);
    }
    
}