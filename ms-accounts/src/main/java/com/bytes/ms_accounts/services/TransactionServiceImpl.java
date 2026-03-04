package com.bytes.ms_accounts.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import com.bytes.ms_accounts.dtos.TransactionDTO;
import com.bytes.ms_accounts.enums.TransactionType;
import com.bytes.ms_accounts.mappers.TransactionMapper;
import com.bytes.ms_accounts.models.Transaction;
import com.bytes.ms_accounts.repositories.TransactionRepository;
import com.bytes.ms_accounts.services.impl.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public List<TransactionDTO> getTransactionsByAccount(@NonNull UUID accountId) {
        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId)
                                   .stream()
                                   .map(transactionMapper::toDTO)
                                   .toList();
    }

    public BigDecimal getTodayWithdrawalTotal(@NonNull UUID accountId) {
        LocalDate today = LocalDate.now();
        ZoneId zoneId = ZoneId.systemDefault();
        
        Instant startOfDay = today.atStartOfDay(zoneId).toInstant();
        Instant endOfDay = today.plusDays(1).atStartOfDay(zoneId).toInstant();

        BigDecimal total = transactionRepository.sumWithdrawalsByAccountInDateRange(
            accountId,
            TransactionType.WITHDRAWAL,
            startOfDay,
            endOfDay
        );

        return total != null ? total : BigDecimal.ZERO;
    }

    public TransactionDTO createTransaction(@NonNull Transaction transaction) {
        return transactionMapper.toDTO(transactionRepository.save(transaction));
    }
}
