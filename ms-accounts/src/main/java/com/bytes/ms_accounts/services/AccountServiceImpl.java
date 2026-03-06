package com.bytes.ms_accounts.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.bytes.ms_accounts.dtos.AccountRequestDTO;
import com.bytes.ms_accounts.dtos.AccountResponseDTO;
import com.bytes.ms_accounts.dtos.CustomerResponseDTO;
import com.bytes.ms_accounts.dtos.CustomerValidationResponse;
import com.bytes.ms_accounts.enums.CustomerStatus;
import com.bytes.ms_accounts.enums.StatusType;
import com.bytes.ms_accounts.exceptions.AccountOwnershipException;
import com.bytes.ms_accounts.exceptions.BusinessException;
import com.bytes.ms_accounts.exceptions.ResourceNotFoundException;
import com.bytes.ms_accounts.mappers.AccountMapper;
import com.bytes.ms_accounts.models.Account;
import com.bytes.ms_accounts.repositories.AccountRepository;
import com.bytes.ms_accounts.services.impl.AccountService;
import org.springframework.lang.NonNull;
import com.bytes.ms_accounts.clients.CustomerClient;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    // Foreign key para validar que el customer existe y está activo
    private final CustomerClient customerClient;
    private final Random random = new Random();
    
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper, CustomerClient customerClient) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.customerClient = customerClient;
    }


    public AccountResponseDTO createAccount(@NonNull AccountRequestDTO request, @NonNull UUID customerId) {
        // Validar que el customer existe y está activo
        CustomerResponseDTO customer = customerClient.getCustomerById(customerId);
        if (!customer.status().equals(CustomerStatus.ACTIVE))
            throw new BusinessException(String.format("Customer %s no está activo", customerId));

        // Máximo 3 cuentas por cliente
        if (accountRepository.countByCustomerId(customerId) >= 3)
            throw new BusinessException(String.format("Número máximo de cuentas alcanzado para el cliente %s", customerId));

        Account account = accountMapper.toEntity(request);
        account.setCustomerId(customerId);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(StatusType.ACTIVE);
        account.setCreatedAt(Instant.now());
        account.setAccountNumber(generateIBAN());

        return accountMapper.toDTO(accountRepository.save(account));
    }

    // Asumimos que es un IBAN español con formato ES00 + 20 dígitos
    private String generateIBAN() {
        String iban;
        do {
            String digits = String.format("%020d", Math.abs(new BigInteger(64, this.random).longValue()) % 99999999999999999L);
            iban = "ES00" + digits;
            // Siempre que no sea único, se genera otro
        } while (accountRepository.existsByAccountNumber(iban));
        return iban;
    }


    public List<AccountResponseDTO> getAccounts(@NonNull UUID customerUuid) {

        // Si el cliente no existe o no está activo, lanzamos una excepción
        CustomerValidationResponse customerValidation = customerClient.validateCustomer(customerUuid);
        if (!customerValidation.exists())
            throw new ResourceNotFoundException(String.format("Customer %s no existe", customerUuid));

        if (!customerValidation.isActive())
            throw new BusinessException(String.format("Customer %s no está activo", customerUuid));
        
        // Obtenemos todas las cuentas filtramos por las que sean de ese customer y obtenemos la lista de los DTO de las cuentas
        return accountRepository.findByCustomerId(customerUuid)
                                        .stream()
                                        .map(accountMapper::toDTO)
                                        .toList();
    }

    public AccountResponseDTO getAccountById(@NonNull UUID accountId, @NonNull UUID customerId) {
        Optional<Account> account = accountRepository.findById(accountId);

        // Verificamos que la cuenta exista y que pertenezca al cliente autenticado
        if (!account.isPresent())
            throw new ResourceNotFoundException(String.format("Account %s no existe", accountId));

        if (!account.get().getCustomerId().equals(customerId))
            throw new AccountOwnershipException(String.format("Account %s no pertenece al customer %s", accountId, customerId));

        return accountMapper.toDTO(account.get());
    }

}
