package com.example.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  
    Page<Employee> findAll(Pageable pageable);
} 
