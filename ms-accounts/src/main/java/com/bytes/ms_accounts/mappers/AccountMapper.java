package com.bytes.ms_accounts.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.bytes.ms_accounts.dtos.AccountRequestDTO;
import com.bytes.ms_accounts.dtos.AccountResponseDTO;
import com.bytes.ms_accounts.models.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dailyWithdrawalLimit", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "currency", expression = "java(request.getCurrency().getCurrencyCode())")
    Account toEntity(AccountRequestDTO request);

    @Mapping(target = "currency", expression = "java(java.util.Currency.getInstance(account.getCurrency()))")
    AccountResponseDTO toDTO(Account account);
}
