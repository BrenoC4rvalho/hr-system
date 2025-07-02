package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    
    @Query("SELECT e FROM Employee e WHERE MONTH(e.birthDate) = :month")
    List<Employee> findByBirthMonth(@Param("month") int month);
    
    @Query("SELECT e.status, COUNT(e) FROM Employee e GROUP BY e.status")
    List<Object[]> countEmployeesByStatus();

    @Query("SELECT e.shift, COUNT(e) FROM Employee e WHERE e.shift <> 'NONE' GROUP BY e.shift")
    List<Object[]> countEmployeesByShift();
} 
