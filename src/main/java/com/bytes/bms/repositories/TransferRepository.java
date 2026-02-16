package com.bytes.bms.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bytes.bms.models.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
