package com.bytes.ms_accounts.repositories;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.bytes.ms_accounts.enums.TransactionType;
import com.bytes.ms_accounts.enums.TransactionStatus;
import com.bytes.ms_accounts.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccountId(UUID accountId);

    List<Transaction> findByAccountIdOrderByCreatedAtDesc(UUID accountId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.accountId = :accountId AND t.type = :type AND t.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumWithdrawalsByAccountInDateRange(
        @Param("accountId") UUID accountId,
        @Param("type") TransactionType type,
        @Param("startDate") Instant startDate,
        @Param("endDate") Instant endDate
    );

    @Query("SELECT t FROM Transaction t WHERE t.accountId = :accountId " +
           "AND (:type IS NULL OR t.type = :type) " +
           "AND (:fromDate IS NULL OR t.createdAt >= :fromDate) " +
           "AND (:toDate IS NULL OR t.createdAt <= :toDate) " +
           "AND t.status = :status " +
           "ORDER BY t.createdAt DESC")
    Page<Transaction> findByAccountIdWithFilters(
        @Param("accountId") UUID accountId,
        @Param("type") TransactionType type,
        @Param("fromDate") Instant fromDate,
        @Param("toDate") Instant toDate,
        @Param("status") TransactionStatus status,
        Pageable pageable
    );
}
