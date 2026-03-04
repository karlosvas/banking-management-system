package com.bytes.ms_accounts.services.impl;

import com.bytes.ms_accounts.dtos.AccountDTO;
import com.bytes.ms_accounts.dtos.RequestAccountDTO;
import com.bytes.ms_accounts.dtos.TransactionDTO;
import com.bytes.ms_accounts.dtos.WithdrawalRequestDTO;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;

public interface AccountService {
    AccountDTO createAccount(@NonNull RequestAccountDTO request, @NonNull UUID customerId);
    List<AccountDTO> getAccounts(@NonNull UUID customerUuid);
    AccountDTO getAccountById(@NonNull UUID accountId, @NonNull UUID customerId);
    TransactionDTO withdraw(@NonNull UUID accountId, @NonNull UUID customerId, @NonNull WithdrawalRequestDTO request);
}