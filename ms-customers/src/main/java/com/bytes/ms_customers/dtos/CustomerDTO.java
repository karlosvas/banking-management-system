package com.bytes.ms_customers.dtos;

import java.util.UUID;
import com.bytes.ms_customers.enums.CustomerStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private UUID id;
    private String dni;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private CustomerStatus status;
    private String createdAt;
}