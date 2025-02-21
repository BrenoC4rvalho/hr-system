package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
 
    Page<User> findAll(Pageable pageable);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmployeeId(Long employeeId);
    
}
