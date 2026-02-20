package com.bytes.ms_customers.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bytes.ms_customers.dtos.CustomerDTO;
import com.bytes.ms_customers.dtos.RegisterRequestDTO;
import com.bytes.ms_customers.dtos.RegisterResponseDTO;
import com.bytes.ms_customers.enums.CustomerStatus;
import com.bytes.ms_customers.models.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "role", constant = "CUSTOMER")
    Customer toCustomer(RegisterRequestDTO dto, CustomerStatus status);

    @Mapping(target = "fullName", expression = "java(customer.getFirstName() + \" \" + customer.getLastName())")
    RegisterResponseDTO toRegisterResponse(Customer customer);

    CustomerDTO toCustomerDTO(Customer customer);
}