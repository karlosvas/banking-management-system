package com.bytes.bms.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bytes.bms.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
