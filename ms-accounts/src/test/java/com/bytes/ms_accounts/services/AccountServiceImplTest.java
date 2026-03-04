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
import com.bytes.ms_accounts.dtos.TransactionDTO;
import com.bytes.ms_accounts.dtos.WithdrawalRequestDTO;
import com.bytes.ms_accounts.enums.StatusType;
import com.bytes.ms_accounts.enums.TransactionType;
import com.bytes.ms_accounts.exceptions.BusinessException;
import com.bytes.ms_accounts.mappers.AccountMapper;
import com.bytes.ms_accounts.models.Account;
import com.bytes.ms_accounts.models.Transaction;
import com.bytes.ms_accounts.repositories.AccountRepository;

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

    @InjectMocks
    private AccountServiceImpl accountService;

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
            // Arrange
            BigDecimal initialBalance = new BigDecimal("1000.00");
            BigDecimal withdrawalAmount = new BigDecimal("100.00");
            BigDecimal expectedNewBalance = new BigDecimal("900.00");
            BigDecimal todayWithdrawals = new BigDecimal("200.00");

            testAccount.setBalance(initialBalance);
            testAccount.setDailyWithdrawalLimit(new BigDecimal("500.00"));

            TransactionDTO expectedTransaction = createTransactionDTO(accountId, withdrawalAmount, expectedNewBalance);

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.getTodayWithdrawalTotal(accountId)).thenReturn(todayWithdrawals);
            when(transactionService.createTransaction(any(Transaction.class))).thenReturn(expectedTransaction);

            // Act
            TransactionDTO result = accountService.withdraw(accountId, customerId, withdrawalRequest);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getAccountId()).isEqualTo(accountId);
            assertThat(result.getAmount()).isEqualTo(withdrawalAmount);
            assertThat(result.getBalanceAfter()).isEqualTo(expectedNewBalance);
            assertThat(result.getType()).isEqualTo(TransactionType.WITHDRAWAL.name());

            // Verify account was updated
            ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
            verify(accountRepository).save(accountCaptor.capture());
            Account savedAccount = accountCaptor.getValue();
            assertThat(savedAccount.getBalance()).isEqualTo(expectedNewBalance);
            assertThat(savedAccount.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should throw BusinessException when account does not exist")
        void withdraw_WithNonExistentAccount_ShouldThrowBusinessException() {
            // Arrange
            when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> accountService.withdraw(accountId, customerId, withdrawalRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Account")
                    .hasMessageContaining("does not exist");

            // Verify no transaction or account update was made
            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionService, never()).createTransaction(any(Transaction.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when account does not belong to customer")
        void withdraw_WithWrongCustomer_ShouldThrowBusinessException() {
            // Arrange
            UUID wrongCustomerId = UUID.randomUUID();
            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));

            // Act & Assert
            assertThatThrownBy(() -> accountService.withdraw(accountId, wrongCustomerId, withdrawalRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("does not belong to customer");

            // Verify no transaction or account update was made
            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionService, never()).createTransaction(any(Transaction.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when account is not active")
        void withdraw_WithInactiveAccount_ShouldThrowBusinessException() {
            // Arrange
            testAccount.setStatus(StatusType.INACTIVE);
            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));

            // Act & Assert
            assertThatThrownBy(() -> accountService.withdraw(accountId, customerId, withdrawalRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("is not active");

            // Verify no transaction or account update was made
            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionService, never()).createTransaction(any(Transaction.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when insufficient balance")
        void withdraw_WithInsufficientBalance_ShouldThrowBusinessException() {
            // Arrange
            testAccount.setBalance(new BigDecimal("50.00"));
            withdrawalRequest.setAmount(new BigDecimal("100.00"));

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));

            // Act & Assert
            assertThatThrownBy(() -> accountService.withdraw(accountId, customerId, withdrawalRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Insufficient balance");

            // Verify no transaction or account update was made
            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionService, never()).createTransaction(any(Transaction.class));
        }

        @Test
        @DisplayName("Should throw BusinessException when daily withdrawal limit exceeded")
        void withdraw_WhenExceedingDailyLimit_ShouldThrowBusinessException() {
            // Arrange
            BigDecimal dailyLimit = new BigDecimal("500.00");
            BigDecimal todayWithdrawals = new BigDecimal("450.00");
            BigDecimal withdrawalAmount = new BigDecimal("100.00"); // Would total 550, exceeding 500 limit

            testAccount.setBalance(new BigDecimal("1000.00"));
            testAccount.setDailyWithdrawalLimit(dailyLimit);
            withdrawalRequest.setAmount(withdrawalAmount);

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.getTodayWithdrawalTotal(accountId)).thenReturn(todayWithdrawals);

            // Act & Assert
            assertThatThrownBy(() -> accountService.withdraw(accountId, customerId, withdrawalRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Daily withdrawal limit exceeded");

            // Verify no transaction or account update was made
            verify(accountRepository, never()).save(any(Account.class));
            verify(transactionService, never()).createTransaction(any(Transaction.class));
        }

        @Test
        @DisplayName("Should successfully withdraw when at exact daily limit")
        void withdraw_WithExactDailyLimit_ShouldSucceed() {
            // Arrange
            BigDecimal dailyLimit = new BigDecimal("500.00");
            BigDecimal todayWithdrawals = new BigDecimal("400.00");
            BigDecimal withdrawalAmount = new BigDecimal("100.00"); // Exactly reaches 500 limit
            BigDecimal initialBalance = new BigDecimal("1000.00");
            BigDecimal expectedNewBalance = new BigDecimal("900.00");

            testAccount.setBalance(initialBalance);
            testAccount.setDailyWithdrawalLimit(dailyLimit);
            withdrawalRequest.setAmount(withdrawalAmount);

            TransactionDTO expectedTransaction = createTransactionDTO(accountId, withdrawalAmount, expectedNewBalance);

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.getTodayWithdrawalTotal(accountId)).thenReturn(todayWithdrawals);
            when(transactionService.createTransaction(any(Transaction.class))).thenReturn(expectedTransaction);

            // Act
            TransactionDTO result = accountService.withdraw(accountId, customerId, withdrawalRequest);

            // Assert
            assertThat(result).isNotNull();
            verify(accountRepository).save(any(Account.class));
            verify(transactionService).createTransaction(any(Transaction.class));
        }

        @Test
        @DisplayName("Should withdraw full account balance")
        void withdraw_WithFullBalance_ShouldSucceed() {
            // Arrange
            BigDecimal accountBalance = new BigDecimal("250.00");
            BigDecimal withdrawalAmount = new BigDecimal("250.00");
            BigDecimal expectedNewBalance = BigDecimal.ZERO;

            testAccount.setBalance(accountBalance);
            testAccount.setDailyWithdrawalLimit(new BigDecimal("500.00"));
            withdrawalRequest.setAmount(withdrawalAmount);

            TransactionDTO expectedTransaction = createTransactionDTO(accountId, withdrawalAmount, expectedNewBalance);

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.getTodayWithdrawalTotal(accountId)).thenReturn(BigDecimal.ZERO);
            when(transactionService.createTransaction(any(Transaction.class))).thenReturn(expectedTransaction);

            // Act
            TransactionDTO result = accountService.withdraw(accountId, customerId, withdrawalRequest);

            // Assert
            assertThat(result).isNotNull();
            ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
            verify(accountRepository).save(accountCaptor.capture());
            assertThat(accountCaptor.getValue().getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Should handle withdrawal with no prior withdrawals today")
        void withdraw_WithNoPriorWithdrawalsToday_ShouldSucceed() {
            // Arrange
            BigDecimal withdrawalAmount = new BigDecimal("100.00");
            BigDecimal initialBalance = new BigDecimal("1000.00");
            BigDecimal expectedNewBalance = new BigDecimal("900.00");

            testAccount.setBalance(initialBalance);
            testAccount.setDailyWithdrawalLimit(new BigDecimal("500.00"));
            withdrawalRequest.setAmount(withdrawalAmount);

            TransactionDTO expectedTransaction = createTransactionDTO(accountId, withdrawalAmount, expectedNewBalance);

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.getTodayWithdrawalTotal(accountId)).thenReturn(BigDecimal.ZERO);
            when(transactionService.createTransaction(any(Transaction.class))).thenReturn(expectedTransaction);

            // Act
            TransactionDTO result = accountService.withdraw(accountId, customerId, withdrawalRequest);

            // Assert
            assertThat(result).isNotNull();
            verify(transactionService).getTodayWithdrawalTotal(accountId);
        }

        @Test
        @DisplayName("Should generate unique reference number for each withdrawal")
        void withdraw_ShouldGenerateUniqueReferenceNumber() {
            // Arrange
            testAccount.setBalance(new BigDecimal("1000.00"));
            testAccount.setDailyWithdrawalLimit(new BigDecimal("500.00"));

            TransactionDTO expectedTransaction = createTransactionDTO(accountId, withdrawalRequest.getAmount(), new BigDecimal("900.00"));

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.getTodayWithdrawalTotal(accountId)).thenReturn(BigDecimal.ZERO);
            when(transactionService.createTransaction(any(Transaction.class))).thenReturn(expectedTransaction);

            // Act
            accountService.withdraw(accountId, customerId, withdrawalRequest);

            // Assert
            ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
            verify(transactionService).createTransaction(transactionCaptor.capture());
            Transaction capturedTransaction = transactionCaptor.getValue();
            
            assertThat(capturedTransaction.getReferenceNumber()).isNotNull();
            assertThat(capturedTransaction.getReferenceNumber()).startsWith("REF-");
        }

        @Test
        @DisplayName("Should set transaction type as WITHDRAWAL")
        void withdraw_ShouldSetCorrectTransactionType() {
            // Arrange
            testAccount.setBalance(new BigDecimal("1000.00"));
            testAccount.setDailyWithdrawalLimit(new BigDecimal("500.00"));

            TransactionDTO expectedTransaction = createTransactionDTO(accountId, withdrawalRequest.getAmount(), new BigDecimal("900.00"));

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.getTodayWithdrawalTotal(accountId)).thenReturn(BigDecimal.ZERO);
            when(transactionService.createTransaction(any(Transaction.class))).thenReturn(expectedTransaction);

            // Act
            accountService.withdraw(accountId, customerId, withdrawalRequest);

            // Assert
            ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
            verify(transactionService).createTransaction(transactionCaptor.capture());
            Transaction capturedTransaction = transactionCaptor.getValue();
            
            assertThat(capturedTransaction.getType()).isEqualTo(TransactionType.WITHDRAWAL);
        }

        @Test
        @DisplayName("Should include withdrawal description in transaction")
        void withdraw_ShouldIncludeDescriptionInTransaction() {
            // Arrange
            String customDescription = "Emergency withdrawal";
            testAccount.setBalance(new BigDecimal("1000.00"));
            testAccount.setDailyWithdrawalLimit(new BigDecimal("500.00"));
            withdrawalRequest.setDescription(customDescription);

            TransactionDTO expectedTransaction = createTransactionDTO(accountId, withdrawalRequest.getAmount(), new BigDecimal("900.00"));

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.getTodayWithdrawalTotal(accountId)).thenReturn(BigDecimal.ZERO);
            when(transactionService.createTransaction(any(Transaction.class))).thenReturn(expectedTransaction);

            // Act
            accountService.withdraw(accountId, customerId, withdrawalRequest);

            // Assert
            ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
            verify(transactionService).createTransaction(transactionCaptor.capture());
            Transaction capturedTransaction = transactionCaptor.getValue();
            
            assertThat(capturedTransaction.getConcept()).isEqualTo(customDescription);
        }

        @Test
        @DisplayName("Should withdraw with small decimal amounts")
        void withdraw_WithSmallDecimalAmount_ShouldSucceed() {
            // Arrange
            BigDecimal withdrawalAmount = new BigDecimal("0.01"); // Minimum withdrawal
            BigDecimal initialBalance = new BigDecimal("100.00");
            BigDecimal expectedNewBalance = new BigDecimal("99.99");

            testAccount.setBalance(initialBalance);
            testAccount.setDailyWithdrawalLimit(new BigDecimal("500.00"));
            withdrawalRequest.setAmount(withdrawalAmount);

            TransactionDTO expectedTransaction = createTransactionDTO(accountId, withdrawalAmount, expectedNewBalance);

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.getTodayWithdrawalTotal(accountId)).thenReturn(BigDecimal.ZERO);
            when(transactionService.createTransaction(any(Transaction.class))).thenReturn(expectedTransaction);

            // Act
            TransactionDTO result = accountService.withdraw(accountId, customerId, withdrawalRequest);

            // Assert
            assertThat(result).isNotNull();
        }
    }

    // Helper methods
    private Account createTestAccount() {
        Account account = new Account();
        account.setId(accountId);
        account.setCustomerId(customerId);
        account.setAccountNumber("ES0012345678901234567890");
        account.setBalance(new BigDecimal("1000.00"));
        account.setDailyWithdrawalLimit(new BigDecimal("500.00"));
        account.setStatus(StatusType.ACTIVE);
        account.setCreatedAt(Instant.now());
        return account;
    }

    private TransactionDTO createTransactionDTO(UUID accountId, BigDecimal amount, BigDecimal balanceAfter) {
        return TransactionDTO.builder()
                .id(UUID.randomUUID())
                .accountId(accountId)
                .type(TransactionType.WITHDRAWAL.name())
                .amount(amount)
                .balanceAfter(balanceAfter)
                .concept("ATM withdrawal")
                .referenceNumber("REF-" + System.currentTimeMillis())
                .status(StatusType.ACTIVE.name())
                .createdAt(Instant.now())
                .build();
    }
}
