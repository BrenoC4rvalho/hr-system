package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.backend.model.User;
import com.example.backend.enums.UserRole;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
 
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmployeeId(Long employeeId);

    boolean existsByRole(UserRole role);
    
}
