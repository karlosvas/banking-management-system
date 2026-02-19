package com.bytes.ms_customers.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.bytes.ms_customers.dtos.RegisterRequestDTO;
import com.bytes.ms_customers.dtos.RegisterResponseDTO;
import com.bytes.ms_customers.enums.CustomerStatus;
import com.bytes.ms_customers.services.CustomerService;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    // ─── Happy path ───────────────────────────────────────────────────────────
    @Test
    void registerCustomer_WithValidData_Returns200WithBody() throws Exception {
        RegisterResponseDTO response = buildValidResponse();
        when(customerService.registerCustomer(any(RegisterRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(buildValidRequest())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.dni").value("12345678A"))
            .andExpect(jsonPath("$.fullName").value("Juan Pérez"))
            .andExpect(jsonPath("$.email").value("juan.perez@example.com"))
            .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    // ─── Validaciones de @Valid ───────────────────────────────────────────────

    @Test
    void registerCustomer_WithBlankDni_Returns400() throws Exception {
        RegisterRequestDTO request = buildValidRequest();
        request.setDni("");

        mockMvc.perform(post("/api/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void registerCustomer_WithInvalidDniFormat_Returns400() throws Exception {
        RegisterRequestDTO request = buildValidRequest();
        request.setDni("1234a"); // no cumple ^\\d{8}[A-Z]$

        mockMvc.perform(post("/api/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void registerCustomer_WithInvalidEmail_Returns400() throws Exception {
        RegisterRequestDTO request = buildValidRequest();
        request.setEmail("not-an-email");

        mockMvc.perform(post("/api/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void registerCustomer_WithWeakPassword_Returns400() throws Exception {
        RegisterRequestDTO request = buildValidRequest();
        request.setPassword("weak"); // no cumple el pattern

        mockMvc.perform(post("/api/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void registerCustomer_WithInvalidPhone_Returns400() throws Exception {
        RegisterRequestDTO request = buildValidRequest();
        request.setPhone("abc123"); // no cumple ^\\+?\\d{7,15}$

        mockMvc.perform(post("/api/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void registerCustomer_WithNullFirstName_Returns400() throws Exception {
        RegisterRequestDTO request = buildValidRequest();
        request.setFirstName(null);

        mockMvc.perform(post("/api/customer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private RegisterRequestDTO buildValidRequest() {
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

    private RegisterResponseDTO buildValidResponse() {
        return RegisterResponseDTO.builder()
            .id(UUID.randomUUID())
            .dni("12345678A")
            .fullName("Juan Pérez")
            .email("juan.perez@example.com")
            .status(CustomerStatus.ACTIVE)
            .createdAt(Instant.now())
            .build();
    }
}