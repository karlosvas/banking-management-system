package com.bytes.ms_accounts.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.bytes.ms_accounts.dtos.AccountDTO;
import com.bytes.ms_accounts.dtos.RequestAccountDTO;
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
    Account toEntity(RequestAccountDTO request);

    @Mapping(target = "currency", expression = "java(java.util.Currency.getInstance(account.getCurrency()))")
    AccountDTO toDTO(Account account);
}
