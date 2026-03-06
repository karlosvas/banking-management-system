package com.bytes.ms_accounts.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import com.bytes.ms_accounts.dtos.TransactionDTO;
import com.bytes.ms_accounts.dtos.TransactionHistoryItemDTO;
import com.bytes.ms_accounts.dtos.TransactionHistoryRequestDTO;
import com.bytes.ms_accounts.dtos.TransactionHistoryResponseDTO;
import com.bytes.ms_accounts.enums.TransactionStatus;
import com.bytes.ms_accounts.enums.TransactionType;
import com.bytes.ms_accounts.mappers.TransactionMapper;
import com.bytes.ms_accounts.models.Transaction;
import com.bytes.ms_accounts.repositories.TransactionRepository;
import com.bytes.ms_accounts.services.impl.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(
        TransactionRepository transactionRepository,
        TransactionMapper transactionMapper
    ) {
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

    public TransactionHistoryResponseDTO getTransactionHistory(@NonNull UUID accountId, @NonNull UUID customerId, @NonNull TransactionHistoryRequestDTO filters) {

        int page = filters.getPage() != null ? filters.getPage() : 0;
        int size = filters.getSize() != null ? filters.getSize() : 20;

        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Transaction> transactionsPage = transactionRepository.findByAccountIdWithFilters(
            accountId,
            filters.getType(),
            filters.getFromDate(),
            filters.getToDate(),
            TransactionStatus.COMPLETED,
            pageable
        );

        List<TransactionHistoryItemDTO> content = transactionsPage.getContent()
            .stream()
            .map(transactionMapper::toHistoryItemDTO)
            .toList();

        return new TransactionHistoryResponseDTO(
            content,
            transactionsPage.getNumber(),
            transactionsPage.getSize(),
            transactionsPage.getTotalElements(),
            transactionsPage.getTotalPages()
        );
    }

    public TransactionDTO createTransaction(@NonNull Transaction transaction) {
        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDTO(savedTransaction);
    }
}
