package com.bytes.bms.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bytes.bms.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
