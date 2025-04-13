package com.example.backend.dto;

import com.example.backend.model.Employee;

public class DepartmentDTO {
    
    private Long id;
    private String name;
    private Employee manager;
    private int numberOfEmployees;

    public DepartmentDTO(Long id, String name, Employee manager, int numberOfEmployees) {
        this.id = id;
        this.name = name;
        this.manager = manager;
        this.numberOfEmployees = numberOfEmployees;
    }

    public DepartmentDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }
    
}
