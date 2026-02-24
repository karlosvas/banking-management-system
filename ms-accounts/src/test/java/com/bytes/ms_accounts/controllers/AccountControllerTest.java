package com.bytes.ms_accounts.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.bytes.ms_accounts.dtos.AccountDTO;
import com.bytes.ms_accounts.dtos.RequestAccountDTO;
import com.bytes.ms_accounts.security.JwtUtils;
import com.bytes.ms_accounts.services.AccountServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountServiceImpl accountService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AccountController accountController;

    @Test
    void testCreateAccount() {
        // GIVEN: Datos de prueba
        UUID customerId = UUID.randomUUID();
        RequestAccountDTO requestDto = new RequestAccountDTO();
        AccountDTO expectedAccount = new AccountDTO();
        
        when(jwtUtils.getCustomerIdFromRequest(request)).thenReturn(customerId);
        when(accountService.createAccount(requestDto, customerId)).thenReturn(expectedAccount);

        // WHEN: Llamamos al método
        ResponseEntity<AccountDTO> response = accountController.createAccount(requestDto, request);

        // THEN: Verificamos resultados
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedAccount, response.getBody());
    }

    @Test
    void testGetAccounts() {
        // GIVEN
        UUID customerId = UUID.randomUUID();
        List<AccountDTO> list = List.of(new AccountDTO(), new AccountDTO());

        when(jwtUtils.getCustomerIdFromRequest(request)).thenReturn(customerId);
        when(accountService.getAccounts(customerId)).thenReturn(list);

        // WHEN
        ResponseEntity<List<AccountDTO>> response = accountController.getAccount(request);

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAccountById() {
        // GIVEN
        UUID customerId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        AccountDTO account = new AccountDTO();

        when(jwtUtils.getCustomerIdFromRequest(request)).thenReturn(customerId);
        when(accountService.getAccountById(accountId, customerId)).thenReturn(account);

        // WHEN
        ResponseEntity<AccountDTO> response = accountController.getAccountById(accountId, request);

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}