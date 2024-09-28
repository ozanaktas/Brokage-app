package com.firm.Brokage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firm.Brokage.entity.UserTable;

@Repository
public interface UserRepository extends JpaRepository<UserTable, Long> {
    Optional<UserTable> findByUsername(String username);
    boolean existsByUsername(String username);
}
