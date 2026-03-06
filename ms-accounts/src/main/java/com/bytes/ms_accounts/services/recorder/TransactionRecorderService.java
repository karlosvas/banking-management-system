package com.bytes.ms_accounts.services.recorder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import com.bytes.ms_accounts.dtos.WithdrawalRequestDTO;
import com.bytes.ms_accounts.enums.TransactionStatus;
import com.bytes.ms_accounts.enums.TransactionType;
import com.bytes.ms_accounts.models.Transaction;
import com.bytes.ms_accounts.services.TransactionServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TransactionRecorderService {

    private final TransactionServiceImpl transactionServiceImpl;

    public TransactionRecorderService(TransactionServiceImpl transactionServiceImpl) {
        this.transactionServiceImpl = transactionServiceImpl;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailedTransactionWithdrawal(UUID accountId, WithdrawalRequestDTO request, String referenceNumber, String reason) {
        Transaction failed = Transaction.builder()
            .accountId(accountId)
            .type(TransactionType.WITHDRAWAL)
            .amount(request.getAmount())
            .concept(reason)
            .balanceAfter(BigDecimal.ZERO)
            .referenceNumber(referenceNumber)
            .status(TransactionStatus.FAILED)
            .createdAt(Instant.now())
            .build();

        log.error("Transaction failed: {}", reason);
        
        transactionServiceImpl.createTransaction(failed);
    }
}
