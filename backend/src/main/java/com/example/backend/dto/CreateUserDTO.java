package com.example.backend.dto;

import org.hibernate.validator.constraints.Length;

import com.example.backend.enums.UserRole;
import com.example.backend.model.Employee;

import jakarta.validation.constraints.NotBlank;

public class CreateUserDTO {

    @NotBlank(message = "The username field cannot be blank.")
    @Length(min = 3, max = 50, message = "The username field between 3 and 50 characters.")
    private String username;
    
    private UserRole role;
    
    private Long employeeId;

    private Employee employee;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

}
