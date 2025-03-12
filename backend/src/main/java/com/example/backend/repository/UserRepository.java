package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.User;
import com.example.backend.enums.UserRole;


public interface UserRepository extends JpaRepository<User, Long> {
 
    Page<User> findAll(Pageable pageable);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmployeeId(Long employeeId);

    boolean existsByRole(UserRole role);
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable );
    
}
