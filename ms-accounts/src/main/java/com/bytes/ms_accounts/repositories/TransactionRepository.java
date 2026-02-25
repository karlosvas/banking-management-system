package com.bytes.ms_accounts.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bytes.ms_accounts.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
