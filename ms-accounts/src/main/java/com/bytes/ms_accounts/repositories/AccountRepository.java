package com.bytes.ms_accounts.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bytes.ms_accounts.models.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    int countByCustomerId(UUID customerId);
    boolean existsByAccountNumber(String accountNumber);
}
