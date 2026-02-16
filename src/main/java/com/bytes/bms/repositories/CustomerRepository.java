package com.bytes.bms.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bytes.bms.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
