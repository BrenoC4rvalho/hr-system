package com.example.backend.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class CreatePositionDTO {

    @Column(nullable = false, unique = true)
    @NotBlank(message = "The name field cannot be blank.")
    @Length(min = 2, max = 100, message = "The name field between 2 and 100 characters.")
    private String name;

    @Length(max = 255, message = "The description field cannot be greater than 255 characters.") 
    private String description;

    public CreatePositionDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CreatePositionDTO() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
