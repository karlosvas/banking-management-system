package com.bytes.bms.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bytes.bms.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
