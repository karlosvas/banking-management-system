package com.bytes.ms_accounts.services;

import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import com.bytes.ms_accounts.dtos.WithdrawalRequestDTO;
import com.bytes.ms_accounts.enums.TransactionStatus;
import com.bytes.ms_accounts.enums.TransactionType;
import com.bytes.ms_accounts.models.Transaction;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionRecorderService {

    private final TransactionServiceImpl transactionServiceImpl;

    public TransactionRecorderService(TransactionServiceImpl transactionServiceImpl) {
        this.transactionServiceImpl = transactionServiceImpl;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailedTransaction(UUID accountId, WithdrawalRequestDTO request, String referenceNumber, String reason) {
        Transaction failed = Transaction.builder()
            .accountId(accountId)
            .type(TransactionType.WITHDRAWAL)
            .amount(request.getAmount())
            .concept(reason)
            .referenceNumber(referenceNumber)
            .status(TransactionStatus.FAILED)
            .createdAt(Instant.now())
            .build();
        transactionServiceImpl.createTransaction(failed);
    }
}
