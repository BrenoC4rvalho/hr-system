package com.example.backend.model;

public class Department {

    private int id;
    private String name;
    private int managerId;

    public Department() {
    }

    public Department(int id, String name, int managerId) {
        this.id = id;
        this.name = name;
        this.managerId = managerId;
    }

    public Department(String name, int managerId) {
        this.name = name;
        this.managerId = managerId;
    }
 
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

}
