package com.bytes.ms_accounts.services.recorder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import com.bytes.ms_accounts.enums.TransactionStatus;
import com.bytes.ms_accounts.enums.TransactionType;
import com.bytes.ms_accounts.enums.TransferStatus;
import com.bytes.ms_accounts.models.Transaction;
import com.bytes.ms_accounts.models.Transfer;
import com.bytes.ms_accounts.repositories.TransferRepository;
import com.bytes.ms_accounts.services.TransactionServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransferRecorderService {
    private final TransferRepository transferRepository;
    private final TransactionServiceImpl transactionServiceImpl;

    public TransferRecorderService(TransferRepository transferRepository, TransactionServiceImpl transactionServiceImpl) {
        this.transferRepository = transferRepository;
        this.transactionServiceImpl = transactionServiceImpl;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailedTransfer(Transfer transfer, String reason) {
        Transfer failedTransfer = Transfer.builder()
            .sourceAccountId(transfer.getSourceAccountId())
            .destinationAccountNumber(transfer.getDestinationAccountNumber())
            .amount(transfer.getAmount())
            .concept("Failed transfer - " + reason)
            .status(TransferStatus.FAILED)
            .createdAt(Instant.now())
            .build();

        log.error("Transfer failed: {}", reason);

        transferRepository.save(failedTransfer);
    }

    public void recordSuccessfulTransferDebit(Transfer transfer, UUID sourceAccountId, BigDecimal totalDebit, BigDecimal sourceBalanceAfter, String targetAccountNumber, String beneficiaryName) {
        Transaction debit = Transaction.builder()
            .accountId(sourceAccountId)
            .type(TransactionType.TRANSFER)
            .amount(totalDebit)
            .balanceAfter(sourceBalanceAfter)
            .concept("Outgoing transfer: " + transfer.getConcept())
            .counterpartyAccountNumber(targetAccountNumber)
            .counterpartyName(beneficiaryName)
            .referenceNumber(transfer.getId() != null ? transfer.getId().toString() : null)
            .status(TransactionStatus.COMPLETED)
            .createdAt(Instant.now())
            .build();

        transactionServiceImpl.createTransaction(debit);
    }

    public void recordSuccessfulTransferCredit(Transfer transfer, UUID targetAccountId, BigDecimal amount, BigDecimal targetBalanceAfter, String sourceAccountNumber) {
        Transaction credit = Transaction.builder()
            .accountId(targetAccountId)
            .type(TransactionType.TRANSFER)
            .amount(amount)
            .balanceAfter(targetBalanceAfter)
            .concept("Incoming transfer: " + transfer.getConcept())
            .counterpartyAccountNumber(sourceAccountNumber)
            .counterpartyName(null)
            .referenceNumber(transfer.getId() != null ? transfer.getId().toString() : null)
            .status(TransactionStatus.COMPLETED)
            .createdAt(Instant.now())
            .build();

        transactionServiceImpl.createTransaction(credit);
    }
}