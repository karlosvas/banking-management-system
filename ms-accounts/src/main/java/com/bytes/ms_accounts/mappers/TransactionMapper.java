package com.bytes.ms_accounts.mappers;

import org.mapstruct.Mapper;
import com.bytes.ms_accounts.dtos.TransactionDTO;
import com.bytes.ms_accounts.models.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toDTO(Transaction transaction);
    Transaction toEntity(TransactionDTO transactionDTO);
}
