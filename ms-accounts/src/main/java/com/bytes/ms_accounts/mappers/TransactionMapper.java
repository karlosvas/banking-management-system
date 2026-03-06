package com.bytes.ms_accounts.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import com.bytes.ms_accounts.dtos.TransactionDTO;
import com.bytes.ms_accounts.dtos.TransactionHistoryItemDTO;
import com.bytes.ms_accounts.dtos.TransactionHistoryResponseDTO;
import com.bytes.ms_accounts.models.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toDTO(Transaction transaction);
    Transaction toEntity(TransactionDTO transactionDTO);

    @Mapping(target = "timestamp", source = "createdAt")
    @Mapping(target = "balance", source = "balanceAfter")
    @Mapping(target = "counterpartyAccount", source = "counterpartyAccountNumber")
    TransactionHistoryItemDTO toHistoryItemDTO(Transaction transaction);

    default TransactionHistoryResponseDTO toHistoryResponseDTO(Page<Transaction> transactionsPage) {
        List<TransactionHistoryItemDTO> content = transactionsPage.getContent()
            .stream()
            .map(this::toHistoryItemDTO)
            .toList();

        return new TransactionHistoryResponseDTO(
            content,
            transactionsPage.getNumber(),
            transactionsPage.getSize(),
            transactionsPage.getTotalElements(),
            transactionsPage.getTotalPages()
        );
    }
}
