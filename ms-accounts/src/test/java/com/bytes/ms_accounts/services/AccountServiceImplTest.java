package com.bytes.ms_accounts.services;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.bytes.ms_accounts.clients.CustomerClient;
import com.bytes.ms_accounts.dtos.AccountDTO;
import com.bytes.ms_accounts.dtos.CustomerDTO;
import com.bytes.ms_accounts.dtos.CustomerValidationResponse;
import com.bytes.ms_accounts.dtos.RequestAccountDTO;
import com.bytes.ms_accounts.enums.CustomerStatus;
import com.bytes.ms_accounts.exceptions.BusinessException;
import com.bytes.ms_accounts.mappers.AccountMapper;
import com.bytes.ms_accounts.models.Account;
import com.bytes.ms_accounts.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock private AccountRepository accountRepository;
    @Mock private AccountMapper accountMapper;
    @Mock private CustomerClient customerClient;

    @InjectMocks
    private AccountServiceImpl accountService;

    private UUID customerId;
    private CustomerDTO activeCustomer;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        activeCustomer = CustomerDTO.builder()
            .id(customerId)
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .phone("123456789")
            .address("Calle Principal 123")
            .status(CustomerStatus.ACTIVE)
            .createdAt(Instant.now())
            .build();
    }

    // ==========================================
    // TESTS PARA: createAccount()
    // ==========================================
    @Test
    void createAccount_Success() {
        // GIVEN
        RequestAccountDTO request = new RequestAccountDTO();
        Account accountEntity = new Account();
        
        when(customerClient.getCustomerById(customerId)).thenReturn(activeCustomer);
        when(accountRepository.countByCustomerId(customerId)).thenReturn((int) 0L);
        when(accountMapper.toEntity(request)).thenReturn(accountEntity);
        // El mock del save devuelve la misma entidad
        when(accountRepository.save(any(Account.class))).thenReturn(accountEntity);
        when(accountMapper.toDTO(any())).thenReturn(new AccountDTO());

        // WHEN
        AccountDTO result = accountService.createAccount(request, customerId);

        // THEN
        assertNotNull(result);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_ThrowsException_WhenCustomerInactive() {
        // GIVEN
        CustomerDTO inactiveCustomer = CustomerDTO.builder()
            .id(customerId)
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .phone("123456789")
            .address("Calle Principal 123")
            .status(CustomerStatus.INACTIVE)
            .createdAt(Instant.now())
            .build();

        when(customerClient.getCustomerById(customerId)).thenReturn(inactiveCustomer);

        // WHEN & THEN
        assertThrows(BusinessException.class, () -> {
            accountService.createAccount(new RequestAccountDTO(), customerId);
        });
    }

    @Test
    void createAccount_ThrowsException_WhenTooManyAccounts() {
        // GIVEN
        when(customerClient.getCustomerById(customerId)).thenReturn(activeCustomer);
        // Ya tiene 3 cuentas
        when(accountRepository.countByCustomerId(customerId)).thenReturn((int) 3L);

        // WHEN & THEN
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            accountService.createAccount(new RequestAccountDTO(), customerId);
        });
        assertTrue(exception.getMessage().contains("Número máximo de cuentas"));
    }

    // ==========================================
    // TESTS PARA: getAccounts()
    // ==========================================

    @Test
    void getAccounts_Success() {
        // GIVEN
        CustomerValidationResponse validationResponse = new CustomerValidationResponse(customerId, true, true);
        when(customerClient.validateCustomer(customerId)).thenReturn(validationResponse);

        Account account1 = new Account();
        Account account2 = new Account();
        List<Account> accountList = List.of(account1, account2);
        when(accountRepository.findByCustomerId(customerId)).thenReturn(accountList);

        when(accountMapper.toDTO(any(Account.class))).thenReturn(new AccountDTO());

        // WHEN
        List<AccountDTO> result = accountService.getAccounts(customerId);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerClient).validateCustomer(customerId);
        verify(accountRepository).findByCustomerId(customerId);
        verify(accountMapper, times(2)).toDTO(any(Account.class));
    }

    @Test
    void getAccounts_ThrowsException_WhenCustomerDoesNotExist() {
        // GIVEN
        // exists = false, isActive = false
        CustomerValidationResponse validationResponse = new CustomerValidationResponse(customerId, false, false);
        when(customerClient.validateCustomer(customerId)).thenReturn(validationResponse);

        // WHEN & THEN
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            accountService.getAccounts(customerId);
        });
        
        assertTrue(exception.getMessage().contains("no existe"));
        verify(accountRepository, never()).findByCustomerId(any());
    }

    @Test
    void getAccounts_ThrowsException_WhenCustomerIsNotActive() {
        // GIVEN
        // exists = true, isActive = false
        CustomerValidationResponse validationResponse = new CustomerValidationResponse(customerId, true, false);
        when(customerClient.validateCustomer(customerId)).thenReturn(validationResponse);

        // WHEN & THEN
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            accountService.getAccounts(customerId);
        });
        
        assertTrue(exception.getMessage().contains("no está activo"));
        verify(accountRepository, never()).findByCustomerId(any());
    }

    // ==========================================
    // TESTS PARA: getAccountById()
    // ==========================================

    @Test
    void getAccountById_Success() {
        // GIVEN
        UUID accountId = UUID.randomUUID();
        Account account = new Account();
        account.setId(accountId);
        account.setCustomerId(customerId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountMapper.toDTO(account)).thenReturn(new AccountDTO());

        // WHEN
        AccountDTO result = accountService.getAccountById(accountId, customerId);

        // THEN
        assertNotNull(result);
        verify(accountRepository).findById(accountId);
        verify(accountMapper).toDTO(account);
    }

    @Test
    void getAccountById_ThrowsException_WhenAccountDoesNotExist() {
        // GIVEN
        UUID accountId = UUID.randomUUID();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // WHEN & THEN
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            accountService.getAccountById(accountId, customerId);
        });

        assertTrue(exception.getMessage().contains("no existe"));
        verify(accountMapper, never()).toDTO(any());
    }

    @Test
    void getAccountById_ThrowsException_WhenAccountBelongsToDifferentCustomer() {
        // GIVEN
        UUID accountId = UUID.randomUUID();
        UUID differentCustomerId = UUID.randomUUID();
        
        Account account = new Account();
        account.setId(accountId);
        // Le asignamos un owner diferente al que la solicita
        account.setCustomerId(differentCustomerId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // WHEN & THEN
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            accountService.getAccountById(accountId, customerId);
        });

        assertTrue(exception.getMessage().contains("no pertenece al customer"));
        verify(accountMapper, never()).toDTO(any());
    }
}