package com.bytes.ms_accounts.controllers;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bytes.ms_accounts.annotations.SwaggerApiResponses;
import com.bytes.ms_accounts.dtos.TransactionDTO;
import com.bytes.ms_accounts.dtos.WithdrawalRequestDTO;
import com.bytes.ms_accounts.dtos.AccountResponseDTO;
import com.bytes.ms_accounts.exceptions.UnauthorizedException;
import com.bytes.ms_accounts.dtos.AccountRequestDTO;
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
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountRequestDTO request, HttpServletRequest httpRequest) {
        UUID customerId = jwtUtils.getCustomerIdFromRequest(httpRequest);

        if (customerId == null)
            throw new UnauthorizedException("Could not determine authenticated customer");

        AccountResponseDTO account = accountService.createAccount(request, customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @Operation(
        summary = "Get all accounts",
        description = "Retrieves all bank accounts for the authenticated customer"
    )
    @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully")
    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAccount(HttpServletRequest httpRequest) {
        UUID customerId = jwtUtils.getCustomerIdFromRequest(httpRequest);
        List<AccountResponseDTO> listAccounts = accountService.getAccounts(customerId);
        return ResponseEntity.ok().body(listAccounts);
    }

    @Operation(
        summary = "Get account by ID",
        description = "Retrieves a specific bank account by its ID for the authenticated customer"
    )
    @ApiResponse(responseCode = "200", description = "Account retrieved successfully")
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable UUID accountId, HttpServletRequest httpRequest) {
        UUID customerId = jwtUtils.getCustomerIdFromRequest(httpRequest);
        AccountResponseDTO account = accountService.getAccountById(accountId, customerId);
        return ResponseEntity.ok().body(account);
    }

    @Operation(
        summary = "Withdraw money from account",
        description = "Withdraws money from the authenticated customer's account. Validates sufficient balance and daily withdrawal limit."
    )
    @ApiResponse(responseCode = "200", description = "Withdrawal successful")
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<TransactionDTO> withdraw(
            @PathVariable UUID accountId,
            @Valid @RequestBody WithdrawalRequestDTO request,
            HttpServletRequest httpRequest) {
        
        UUID customerId = jwtUtils.getCustomerIdFromRequest(httpRequest);
        TransactionDTO transaction = accountService.withdraw(accountId, customerId, request);
        return ResponseEntity.ok().body(transaction);
    }
    
}