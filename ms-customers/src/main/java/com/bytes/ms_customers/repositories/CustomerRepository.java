package com.bytes.ms_customers.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bytes.ms_customers.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
