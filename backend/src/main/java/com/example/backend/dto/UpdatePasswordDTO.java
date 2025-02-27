package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdatePasswordDTO {

    @NotBlank(message = "The password hash field cannot be blank.")
    private String password;
    
    public UpdatePasswordDTO(@NotBlank String password) {
        this.password = password;
    }

    public UpdatePasswordDTO() {
        
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

}

