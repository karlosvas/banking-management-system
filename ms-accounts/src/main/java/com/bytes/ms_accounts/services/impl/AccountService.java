package com.bytes.ms_accounts.services.impl;

import com.bytes.ms_accounts.dtos.AccountRequestDTO;
import com.bytes.ms_accounts.dtos.AccountResponseDTO;
import org.springframework.lang.NonNull;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponseDTO createAccount(@NonNull AccountRequestDTO request, @NonNull UUID customerId);
    List<AccountResponseDTO> getAccounts(@NonNull UUID customerUuid);
    AccountResponseDTO getAccountById(@NonNull UUID accountId, @NonNull UUID customerId);
}