package com.bytes.ms_accounts.clients;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.bytes.ms_accounts.dtos.CustomerResponseDTO;
import com.bytes.ms_accounts.dtos.CustomerValidationResponse;

/**
 * Feign client for communication with the customers microservice.
 *
 * <p>Used by the accounts service to retrieve customer data and validate
 * customer existence/status before account operations.</p>
 */
@FeignClient(name = "ms-customers", url = "${CUSTOMERS_SERVICE_URL:http://localhost:8081}")
public interface CustomerClient {
    /**
     * Retrieves a customer by UUID.
     *
     * @param customerUuid customer identifier
     * @return customer details
     */
    @GetMapping("/api/customers/{customerUuid}")
    CustomerResponseDTO getCustomerById(@PathVariable UUID customerUuid);

    /**
     * Validates whether a customer exists and is active.
     *
     * @param customerId customer identifier
     * @return validation result with existence and active flags
     */
    @GetMapping("/api/customers/{customerId}/validate")
    CustomerValidationResponse validateCustomer(@PathVariable UUID customerId);
}