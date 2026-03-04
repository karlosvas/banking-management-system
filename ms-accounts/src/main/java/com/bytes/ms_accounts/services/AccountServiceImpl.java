package com.bytes.ms_accounts.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.bytes.ms_accounts.dtos.AccountDTO;
import com.bytes.ms_accounts.dtos.CustomerDTO;
import com.bytes.ms_accounts.dtos.CustomerValidationResponse;
import com.bytes.ms_accounts.dtos.RequestAccountDTO;
import com.bytes.ms_accounts.dtos.TransactionDTO;
import com.bytes.ms_accounts.dtos.WithdrawalRequestDTO;
import com.bytes.ms_accounts.enums.AccountStatus;
import com.bytes.ms_accounts.enums.CustomerStatus;
import com.bytes.ms_accounts.enums.StatusType;
import com.bytes.ms_accounts.enums.TransactionStatus;
import com.bytes.ms_accounts.enums.TransactionType;
import com.bytes.ms_accounts.exceptions.BusinessException;
import com.bytes.ms_accounts.mappers.AccountMapper;
import com.bytes.ms_accounts.models.Account;
import com.bytes.ms_accounts.models.Transaction;
import com.bytes.ms_accounts.repositories.AccountRepository;
import com.bytes.ms_accounts.services.impl.AccountService;
import com.bytes.ms_accounts.clients.CustomerClient;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final TransactionServiceImpl transactionServiceImpl;
    // Foreign key to validate that customer exists and is active
    private final CustomerClient customerClient;
    private final Random random = new Random();
    private final TransactionRecorderService transactionRecorderService;
    
    public AccountServiceImpl(
            AccountRepository accountRepository,
            AccountMapper accountMapper,
            TransactionServiceImpl transactionServiceImpl,
            CustomerClient customerClient,
            TransactionRecorderService transactionRecorderService) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.transactionServiceImpl = transactionServiceImpl;
        this.customerClient = customerClient;
        this.transactionRecorderService = transactionRecorderService;
    }

    public AccountDTO createAccount(@NonNull RequestAccountDTO request, @NonNull UUID customerId) {
        // Validate that customer exists and is active
        CustomerDTO customer = customerClient.getCustomerById(customerId);
        if (!customer.getStatus().equals(CustomerStatus.ACTIVE))
            throw new BusinessException(String.format("Customer %s is not active", customerId));

        // Maximum 3 accounts per customer
        if (accountRepository.countByCustomerId(customerId) >= 3)
            throw new BusinessException(String.format("Maximum number of accounts reached for customer %s", customerId));

        Account account = accountMapper.toEntity(request);
        account.setCustomerId(customerId);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedAt(Instant.now());
        account.setAccountNumber(generateIBAN());

        return accountMapper.toDTO(accountRepository.save(account));
    }

    // We assume it's a Spanish IBAN with format ES00 + 20 digits
    private String generateIBAN() {
        String iban;
        do {
            String digits = String.format("%020d", Math.abs(new BigInteger(64, this.random).longValue()) % 99999999999999999L);
            iban = "ES00" + digits;
            // As long as it's not unique, another is generated
        } while (accountRepository.existsByAccountNumber(iban));
        return iban;
    }

    public List<AccountDTO> getAccounts(@NonNull UUID customerUuid) {
        // If customer does not exist or is not active, throw an exception
        CustomerValidationResponse customerValidation = customerClient.validateCustomer(customerUuid);
        if (!customerValidation.exists())
            throw new BusinessException(String.format("Customer %s does not exist", customerUuid));
        if (!customerValidation.isActive())
            throw new BusinessException(String.format("Customer %s is not active", customerUuid));
        
        // Get all accounts filter by those of that customer and get the list of DTOs of the accounts
        return accountRepository.findByCustomerId(customerUuid)
                                        .stream()
                                        .map(accountMapper::toDTO)
                                        .toList();
    }

    public AccountDTO getAccountById(@NonNull UUID accountId, @NonNull UUID customerId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);

        // Verify that the account exists and belongs to the authenticated customer
        if (!accountOpt.isPresent())
            throw new BusinessException(String.format("Account %s does not exist", accountId));

        if (!accountOpt.get().getCustomerId().equals(customerId))
            throw new BusinessException(String.format("Account %s does not belong to customer %s", accountId, customerId));

        return accountMapper.toDTO(accountOpt.get());
    }

    @Transactional
    public TransactionDTO withdraw(@NonNull UUID accountId, @NonNull UUID customerId, @NonNull WithdrawalRequestDTO request) {
        String referenceNumber = generateReferenceNumber();

        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            transactionRecorderService.recordFailedTransaction(accountId, request, referenceNumber, "Account does not exist");
            throw new BusinessException(String.format("Account %s does not exist", accountId));
        }

        Account account = accountOpt.get();

        if (!account.getCustomerId().equals(customerId)) {
            transactionRecorderService.recordFailedTransaction(accountId, request, referenceNumber, "Account does not belong to customer");
            throw new BusinessException(String.format("Account %s does not belong to customer %s", accountId, customerId));
        }

        if (!account.getStatus().equals(AccountStatus.ACTIVE)) {
            transactionRecorderService.recordFailedTransaction(accountId, request, referenceNumber, "Account is not active");
            throw new BusinessException(String.format("Account %s is not active", accountId));
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            transactionRecorderService.recordFailedTransaction(accountId, request, referenceNumber, "Insufficient balance");
            throw new BusinessException(String.format("Insufficient balance. Available: %s, Requested: %s", account.getBalance(), request.getAmount()));
        }

        BigDecimal todayWithdrawalTotal = transactionServiceImpl.getTodayWithdrawalTotal(accountId);
        BigDecimal totalAfter = todayWithdrawalTotal.add(request.getAmount());
        if (totalAfter.compareTo(account.getDailyWithdrawalLimit()) > 0) {
            BigDecimal remaining = account.getDailyWithdrawalLimit().subtract(todayWithdrawalTotal);
            transactionRecorderService.recordFailedTransaction(accountId, request, referenceNumber, "Daily withdrawal limit exceeded");
            throw new BusinessException(String.format("Daily withdrawal limit exceeded. You can withdraw up to %s more today", remaining));
        }

        BigDecimal newBalance = account.getBalance().subtract(request.getAmount());
        account.setBalance(newBalance);
        account.setUpdatedAt(Instant.now());
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
            .accountId(accountId)
            .type(TransactionType.WITHDRAWAL)
            .amount(request.getAmount())
            .balanceAfter(newBalance)
            .concept(request.getDescription())
            .referenceNumber(referenceNumber)
            .status(TransactionStatus.COMPLETED)
            .createdAt(Instant.now())
            .build();

        return transactionServiceImpl.createTransaction(transaction);
    }

    private String generateReferenceNumber() {
        final String REFERENCE_PREFIX = "REF-";
        String actual;

        do {
            actual = REFERENCE_PREFIX + System.currentTimeMillis();
        } while (accountRepository.existsByAccountNumber(actual));

        return actual;
    }

}
