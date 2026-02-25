package com.bytes.ms_accounts.services.impl;

import com.bytes.ms_accounts.dtos.AccountDTO;
import com.bytes.ms_accounts.dtos.RequestAccountDTO;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountDTO createAccount(RequestAccountDTO request, UUID customerId);
    List<AccountDTO> getAccounts(UUID customerUuid);
    AccountDTO getAccountById(UUID accountId, UUID customerId);
}