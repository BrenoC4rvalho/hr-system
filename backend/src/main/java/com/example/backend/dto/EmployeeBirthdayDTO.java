package com.example.backend.dto;

import java.time.LocalDate;

import com.example.backend.model.Department;

public class EmployeeBirthdayDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private Department department;
    private LocalDate birthDate;
    
    public EmployeeBirthdayDTO(Long id, String firstName, String lastName, Department department, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.birthDate = birthDate;
    }

    public EmployeeBirthdayDTO() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Department getDepartment() {
        return department;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

}
