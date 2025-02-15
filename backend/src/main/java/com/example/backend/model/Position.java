package com.example.backend.model;

public class Position {

    private Long id;
    private String name;
    private String description;

    public Position() {
    }

    public Position(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Position(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
