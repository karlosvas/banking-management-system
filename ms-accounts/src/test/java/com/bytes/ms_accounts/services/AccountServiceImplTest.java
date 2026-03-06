package com.bytes.ms_accounts.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bytes.ms_accounts.clients.CustomerClient;
import com.bytes.ms_accounts.dtos.DepositRequestDTO;
import com.bytes.ms_accounts.dtos.DepositResponseDTO;
import com.bytes.ms_accounts.dtos.TransactionDTO;
import com.bytes.ms_accounts.dtos.WithdrawalRequestDTO;
import com.bytes.ms_accounts.enums.AccountStatus;
import com.bytes.ms_accounts.enums.TransactionStatus;
import com.bytes.ms_accounts.enums.TransactionType;
import com.bytes.ms_accounts.exceptions.BusinessException;
import com.bytes.ms_accounts.mappers.AccountMapper;
import com.bytes.ms_accounts.mappers.TransactionMapper;
import com.bytes.ms_accounts.models.Account;
import com.bytes.ms_accounts.models.Transaction;
import com.bytes.ms_accounts.repositories.AccountRepository;
import com.bytes.ms_accounts.services.recorder.TransactionRecorderService;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountServiceImpl Tests")
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private TransactionServiceImpl transactionService;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private TransactionRecorderService transactionRecorderService;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    // --- VARIABLES DE INSTANCIA AÑADIDAS ---
    private UUID accountId;
    private UUID customerId;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        testAccount = createTestAccount();
    }

    @Nested
    @DisplayName("Deposit Method Tests")
    class DepositTests {

        private DepositRequestDTO depositRequest;

        @BeforeEach
        void setUp() {
            depositRequest = DepositRequestDTO.builder()
                .amount(new BigDecimal("100.00"))
                .description("Cash deposit")
                .build();
        }

        @Test
        @DisplayName("Should successfully deposit money when all validations pass")
        void deposit_WithValidRequest_ShouldSucceed() {
            BigDecimal initialBalance = new BigDecimal("1000.00");
            BigDecimal depositAmount = new BigDecimal("100.00");
            BigDecimal expectedNewBalance = new BigDecimal("1100.00");

            testAccount.setBalance(initialBalance);

            TransactionDTO transactionDTO = new TransactionDTO(
                UUID.randomUUID(), accountId, TransactionType.DEPOSIT, depositAmount, expectedNewBalance,
                "Cash deposit", null, null, "REF-" + System.currentTimeMillis(), 
                TransactionStatus.COMPLETED, Instant.now()
            );

            DepositResponseDTO expectedResponse = new DepositResponseDTO(
                transactionDTO.id(),
                TransactionType.DEPOSIT,
                depositAmount,
                initialBalance,
                expectedNewBalance,
                "Cash deposit",
                transactionDTO.createdAt()
            );

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transactionDTO);
            when(transactionMapper.toDepositResponseDTO(transactionDTO, initialBalance)).thenReturn(expectedResponse);

            DepositResponseDTO result = accountService.deposit(accountId, customerId, depositRequest);

            assertThat(result).isNotNull();
            assertThat(result.amount()).isEqualByComparingTo(depositAmount);
            assertThat(result.balanceAfter()).isEqualByComparingTo(expectedNewBalance);
            
            verify(accountRepository).save(any(Account.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when account does not exist")
        void deposit_WithNonExistentAccount_ShouldThrowBusinessException() {
            when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.deposit(accountId, customerId, depositRequest))
                .isInstanceOf(BusinessException.class);

            verify(accountRepository, never()).save(any(Account.class));
        }
    }

    @Nested
    @DisplayName("Withdraw Method Tests")
    class WithdrawTests {

        private WithdrawalRequestDTO withdrawalRequest;

        @BeforeEach
        void setUp() {
            withdrawalRequest = WithdrawalRequestDTO.builder()
                    .amount(new BigDecimal("100.00"))
                    .description("ATM withdrawal")
                    .build();
        }

        @Test
        @DisplayName("Should successfully withdraw money when all validations pass")
        void withdraw_WithValidRequest_ShouldSucceed() {
            BigDecimal initialBalance = new BigDecimal("1000.00");
            BigDecimal withdrawalAmount = new BigDecimal("100.00");
            BigDecimal expectedNewBalance = new BigDecimal("900.00");

            testAccount.setBalance(initialBalance);
            testAccount.setDailyWithdrawalLimit(new BigDecimal("500.00"));

            TransactionDTO expectedTransaction = createTransactionDTO(accountId, withdrawalAmount, expectedNewBalance);

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.getTodayWithdrawalTotal(accountId)).thenReturn(new BigDecimal("200.00"));
            when(transactionService.createTransaction(any(Transaction.class))).thenReturn(expectedTransaction);

            TransactionDTO result = accountService.withdraw(accountId, customerId, withdrawalRequest);

            assertThat(result).isNotNull();
            assertThat(result.balanceAfter()).isEqualByComparingTo(expectedNewBalance);
            verify(accountRepository).save(any(Account.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when insufficient balance")
        void withdraw_WithInsufficientBalance_ShouldThrowBusinessException() {
            testAccount.setBalance(new BigDecimal("50.00"));
            withdrawalRequest.setAmount(new BigDecimal("100.00"));

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));

            assertThatThrownBy(() -> accountService.withdraw(accountId, customerId, withdrawalRequest))
                    .isInstanceOf(BusinessException.class);
        }
    }

    private Account createTestAccount() {
        Account account = new Account();
        account.setId(accountId);
        account.setCustomerId(customerId);
        account.setAccountNumber("ES0012345678901234567890");
        account.setBalance(new BigDecimal("1000.00"));
        account.setDailyWithdrawalLimit(new BigDecimal("500.00"));
        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedAt(Instant.now());
        return account;
    }

    private TransactionDTO createTransactionDTO(UUID accountId, BigDecimal amount, BigDecimal balanceAfter) {
        return new TransactionDTO(
            UUID.randomUUID(),
            accountId,
            TransactionType.WITHDRAWAL,
            amount,
            balanceAfter,
            "ATM withdrawal",
            "ES9121000418450200051332",
            "Bank ATM",
            "REF-" + System.currentTimeMillis(),
            TransactionStatus.COMPLETED,
            Instant.now()
        );
    }
}