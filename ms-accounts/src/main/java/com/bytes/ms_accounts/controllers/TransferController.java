package com.bytes.ms_accounts.controllers;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bytes.ms_accounts.annotations.SwaggerApiResponses;
import com.bytes.ms_accounts.dtos.TransferResponseDTO;
import com.bytes.ms_accounts.exceptions.UnauthorizedException;
import com.bytes.ms_accounts.security.JwtUtils;
import com.bytes.ms_accounts.services.TransferServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import com.bytes.ms_accounts.dtos.TransferReqestDTO;

/**
 * REST controller for transfer operations.
 *
 * <p>Provides endpoints to create transfers between accounts for authenticated users.</p>
 */
@SwaggerApiResponses
@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferServiceImpl transferServiceImpl;
    private final JwtUtils jwtUtils;

    public TransferController (TransferServiceImpl transferServiceImpl, JwtUtils jwtUtils) {
        this.transferServiceImpl = transferServiceImpl;
        this.jwtUtils = jwtUtils;
    }

    @Operation(
        summary = "Create a new transfer",
        description = "Creates a new transfer between accounts for the authenticated customer"
    )
     @ApiResponse(
        responseCode = "201",
        description = "Transfer created successfully"
    )
    @PostMapping
    public ResponseEntity<TransferResponseDTO> createTransfer(@Valid @RequestBody TransferReqestDTO transferRequest, HttpServletRequest httpRequest) {
        UUID customerUuid = jwtUtils.getCustomerIdFromRequest(httpRequest);

        if (customerUuid == null)
            throw new UnauthorizedException("Could not determine authenticated customer");

        return ResponseEntity.status(HttpStatus.CREATED).body(transferServiceImpl.createTransfer(transferRequest, customerUuid));
    }
}
