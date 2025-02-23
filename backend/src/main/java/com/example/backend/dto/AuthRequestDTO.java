package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthRequestDTO {

    @NotBlank(message = "The username field is required.")
    private String username;
    
    @NotBlank(message = "The password field is required.")
    private String password;

    public AuthRequestDTO(
        @NotBlank(message = "The username field is required.") String username,
        @NotBlank(message = "The password field is required.") String password
    ) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
}
