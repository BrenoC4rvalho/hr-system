package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdatePasswordDTO {

    @NotBlank(message = "The password hash field cannot be blank.")
    private String password;
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

