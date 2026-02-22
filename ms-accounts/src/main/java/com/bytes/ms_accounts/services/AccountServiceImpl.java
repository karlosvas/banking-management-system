package com.bytes.ms_accounts.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.bytes.ms_accounts.dtos.AccountDTO;
import com.bytes.ms_accounts.dtos.CustomerDTO;
import com.bytes.ms_accounts.dtos.RequestAccountDTO;
import com.bytes.ms_accounts.enums.CustomerStatus;
import com.bytes.ms_accounts.enums.StatusType;
import com.bytes.ms_accounts.exceptions.BusinessException;
import com.bytes.ms_accounts.mappers.AccountMapper;
import com.bytes.ms_accounts.models.Account;
import com.bytes.ms_accounts.repositories.AccountRepository;
import com.bytes.ms_accounts.services.impl.AccountService;
import com.bytes.ms_accounts.clients.CustomerClient;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerClient customerClient;
    private final Random random = new Random();
    
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper, CustomerClient customerClient) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.customerClient = customerClient;
    }


    public AccountDTO createAccount(RequestAccountDTO request, UUID customerId) {
        // Validar que el customer existe y está activo
        CustomerDTO customer = customerClient.getCustomerById(customerId);
        if (!customer.getStatus().equals(CustomerStatus.ACTIVE))
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
}
