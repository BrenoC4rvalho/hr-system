package com.example.backend.service;

import com.example.backend.repository.EmployeeRepository;

public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
}
