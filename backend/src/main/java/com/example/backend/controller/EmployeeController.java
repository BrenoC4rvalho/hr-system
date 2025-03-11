package com.example.backend.controller;

import com.example.backend.service.EmployeeService;

public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
}
