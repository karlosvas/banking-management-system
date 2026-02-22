package com.bytes.ms_accounts.clients;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.bytes.ms_accounts.dtos.CustomerDTO;

@FeignClient(name = "ms-customers", url = "${CUSTOMERS_SERVICE_URL}")
public interface CustomerClient {
    @GetMapping("/api/customers/{id}")
    CustomerDTO getCustomerById(@PathVariable UUID id);
}