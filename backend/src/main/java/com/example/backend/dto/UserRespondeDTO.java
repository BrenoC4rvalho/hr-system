package com.example.backend.dto;

import com.example.backend.enums.UserRole;
import com.example.backend.enums.UserStatus;
import com.example.backend.model.Employee;


public class UserRespondeDTO {

    private Long id;
    private String username;
    private UserRole role;
    private Employee employee;
    private UserStatus status;

    public UserRespondeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }    

    
    
}
