package com.bytes.bms.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bytes.bms.models.Bank;

public interface BankRepository extends JpaRepository<Bank, Long> {
}
