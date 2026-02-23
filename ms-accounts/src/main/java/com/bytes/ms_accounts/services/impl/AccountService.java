package com.bytes.ms_accounts.services.impl;

import com.bytes.ms_accounts.dtos.AccountDTO;
import com.bytes.ms_accounts.dtos.RequestAccountDTO;
import java.util.UUID;

public interface AccountService {
    AccountDTO createAccount(RequestAccountDTO request, UUID customerId);
}