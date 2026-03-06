package com.bytes.ms_customers.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.bytes.ms_customers.dtos.CustomerResponseDTO;
import com.bytes.ms_customers.dtos.RegisterRequestDTO;
import com.bytes.ms_customers.dtos.RegisterResponseDTO;
import com.bytes.ms_customers.enums.CustomerRole;
import com.bytes.ms_customers.enums.CustomerStatus;
import com.bytes.ms_customers.exceptions.ResourceNotFoundException;
import com.bytes.ms_customers.mappers.CustomerMapper;
import com.bytes.ms_customers.models.Customer;
import com.bytes.ms_customers.repositories.CustomerRepository;
import com.bytes.ms_customers.security.JwtUtils;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void registerCustomer_WithValidData_ShouldReturnRegisteredCustomer() {
        RegisterRequestDTO request = createRegisterRequest();
        Customer customer = createCustomer();
        RegisterResponseDTO expectedResponse = createRegisterResponse();

        when(customerMapper.toCustomer(request, CustomerStatus.ACTIVE)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toRegisterResponse(customer)).thenReturn(expectedResponse);

        RegisterResponseDTO result = customerService.registerCustomer(request);

        assertThat(result).isNotNull();
        assertThat(result.dni()).isEqualTo("12345678A");

        verify(customerRepository).save(customer);
    }

    @Test
    void getCurrentCustomer_WithValidEmail_ShouldReturnCustomerDTO() {
        String email = "juan.perez@example.com";
        Customer customer = createCustomer();
        CustomerResponseDTO expectedDTO = createCustomerResponseDTO();

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));
        when(customerMapper.toCustomerDTO(customer)).thenReturn(expectedDTO);

        CustomerResponseDTO result = customerService.getCurrentCustomer(email);

        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(email);
        assertThat(result.firstName()).isEqualTo("Juan");

        verify(customerRepository).findByEmail(email);
        verify(customerMapper).toCustomerDTO(customer);
    }

    @Test
    void getCurrentCustomer_WithNonExistentEmail_ShouldThrowResourceNotFoundException() {
        String email = "nonexistent@example.com";

        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCurrentCustomer(email))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Cliente");

        verify(customerRepository).findByEmail(email);
    }

    // Helper methods
    private RegisterRequestDTO createRegisterRequest() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setDni("12345678A");
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setEmail("juan.perez@example.com");
        dto.setPassword("Password123");
        dto.setPhone("+34123456789");
        dto.setAddress("Calle Principal 123");
        return dto;
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setDni("12345678A");
        customer.setFirstName("Juan");
        customer.setLastName("Pérez");
        customer.setEmail("juan.perez@example.com");
        customer.setPassword("Password123");
        customer.setPhone("+34123456789");
        customer.setAddress("Calle Principal 123");
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setRole(CustomerRole.CUSTOMER);
        return customer;
    }

    private RegisterResponseDTO createRegisterResponse() {
        return new RegisterResponseDTO(
            UUID.randomUUID(),
            "12345678A",
            "Juan Pérez",
            "juan.perez@example.com",
            CustomerStatus.ACTIVE,
            Instant.now()
        );
    }

    private CustomerResponseDTO createCustomerResponseDTO() {
        return new CustomerResponseDTO(
            UUID.randomUUID(),
            "12345678A",
            "Juan",
            "Pérez",
            "juan.perez@example.com",
            "+34123456789",
            "Calle Principal 123",
            CustomerStatus.ACTIVE,
            Instant.now().toString()
        );
    }

}
