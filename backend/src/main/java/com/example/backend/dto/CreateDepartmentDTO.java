package com.example.backend.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class CreateDepartmentDTO {

    @Column(nullable = false, unique = true)
    @NotBlank(message = "The name field cannot be blank.")
    @Length(min = 2, max = 100, message = "The name field between 2 and 100 characters.")
    private String name;

    public CreateDepartmentDTO(String name) {
        this.name = name;
    }

    public CreateDepartmentDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
