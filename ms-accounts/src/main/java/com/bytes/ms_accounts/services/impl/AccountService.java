package com.bytes.ms_accounts.services.impl;

import com.bytes.ms_accounts.dtos.TransactionDTO;
import com.bytes.ms_accounts.dtos.DepositRequestDTO;
import com.bytes.ms_accounts.dtos.DepositResponseDTO;
import com.bytes.ms_accounts.dtos.WithdrawalRequestDTO;
import com.bytes.ms_accounts.dtos.AccountRequestDTO;
import com.bytes.ms_accounts.dtos.AccountResponseDTO;
import org.springframework.lang.NonNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponseDTO createAccount(@NonNull AccountRequestDTO request, @NonNull UUID customerId);
    List<AccountResponseDTO> getAccounts(@NonNull UUID customerUuid);
    AccountResponseDTO getAccountById(@NonNull UUID accountId);
    AccountResponseDTO getAccountByNumber(@NonNull String accountNumber);
    AccountResponseDTO getAccountByMe(@NonNull UUID accountId, @NonNull UUID customerId);
    DepositResponseDTO deposit(@NonNull UUID accountId, @NonNull UUID customerId, @NonNull DepositRequestDTO request);
    TransactionDTO withdraw(@NonNull UUID accountId, @NonNull UUID customerId, @NonNull WithdrawalRequestDTO request);
    void addMoney(@NonNull UUID accountId, @NonNull BigDecimal amount);
}