package com.example.backend.dto;

import com.example.backend.enums.UserRole;
import com.example.backend.enums.UserStatus;

public class UpdateUserDTO {

    private String username;
    private UserRole role;
    private UserStatus status;
    private Long employeeId;
  
    public UpdateUserDTO(String username, UserRole role, UserStatus status, Long employeeId) {
        this.username = username;
        this.role = role;
        this.status = status;
        this.employeeId = employeeId;
    }

    public UpdateUserDTO() {
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
    public UserStatus getStatus() {
        return status;
    }
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

}
