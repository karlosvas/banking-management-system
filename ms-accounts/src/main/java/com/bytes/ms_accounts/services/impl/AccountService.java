package com.bytes.ms_accounts.services.impl;

import com.bytes.ms_accounts.dtos.TransactionDTO;
import com.bytes.ms_accounts.dtos.WithdrawalRequestDTO;
import com.bytes.ms_accounts.dtos.CreateAccountRequestDTO;
import com.bytes.ms_accounts.dtos.AccountResponseDTO;
import org.springframework.lang.NonNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponseDTO createAccount(@NonNull CreateAccountRequestDTO request, @NonNull UUID customerId);
    List<AccountResponseDTO> getAccounts(@NonNull UUID customerUuid);
    AccountResponseDTO getAccountById(@NonNull UUID accountId);
    AccountResponseDTO getAccountByNumber(@NonNull String accountNumber);
    AccountResponseDTO getAccountByMe(@NonNull UUID accountId, @NonNull UUID customerId);
    TransactionDTO withdraw(@NonNull UUID accountId, @NonNull UUID customerId, @NonNull WithdrawalRequestDTO request);
    void addMoney(@NonNull UUID accountId, @NonNull BigDecimal amount);
}