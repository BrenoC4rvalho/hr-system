package com.example.backend.model;

import com.example.backend.Enum.UserRole;
import com.example.backend.Enum.UserStatus;

public class User {

    private int id;
    private String username;
    private String passwordHash;
    private UserRole role = UserRole.HR;
    private int EmployeeId;
    private UserStatus status = UserStatus.ACTIVE;

    public User() {
    }

    public User(int id, String username, String passwordHash, UserRole role, int EmployeeId, UserStatus status) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.EmployeeId = EmployeeId;
        this.status = status;
    }

    public User(String username, String passwordHash, UserRole role, int EmployeeId) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.EmployeeId = EmployeeId;
    }

    public User(String username, String passwordHash, UserRole role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public int getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(int EmployeeId) {
        this.EmployeeId = EmployeeId;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

}
