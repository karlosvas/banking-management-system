package com.bytes.ms_accounts.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;
import com.bytes.ms_accounts.dtos.TransactionDTO;
import com.bytes.ms_accounts.models.Transaction;

public interface TransactionService {
    List<TransactionDTO> getTransactionsByAccount(@NonNull UUID accountId);
    BigDecimal getTodayWithdrawalTotal(@NonNull UUID accountId);
    TransactionDTO createTransaction(@NonNull Transaction transaction);
}
