package com.bytes.ms_accounts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.bytes.ms_accounts.dtos.ErrorResponseDTO;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manage custom exceptions
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusiness(BusinessException ex) {
        log.warn("Business rule violation: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Business error", ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Not found", ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorized(UnauthorizedException ex) {
        log.warn("Unauthorized access: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage());
    }

    @ExceptionHandler(AccountOwnershipException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccountOwnership(AccountOwnershipException ex) {
        log.warn("Account ownership violation: {}", ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, "Access denied", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error", "An unexpected error occurred");
    }

    // Manage validation errors from @Valid annotations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
            .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}", message);
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation error", message);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponseDTO> handleFeign(FeignException ex) {
        log.error("Feign client error [{}]: {}", ex.status(), ex.getMessage());
        
        if (ex.status() == 404)
            return buildResponse(HttpStatus.NOT_FOUND, "Not found", "Customer does not exist");
        if (ex.status() == 403)
            return buildResponse(HttpStatus.FORBIDDEN, "Access denied", "You do not have permission to access this resource");
            
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable", "Error communicating with ms-customers");
    }

    // Method to build consistent error responses across all handlers
    private ResponseEntity<ErrorResponseDTO> buildResponse(@NonNull HttpStatusCode status, String error, String message) {
        return ResponseEntity.status(status)
            .body(new ErrorResponseDTO(error, message, Instant.now(), status.value()));
    }
}