package com.example.backend.dto;

import java.time.LocalDate;

import com.example.backend.enums.EmployeeStatus;
import com.example.backend.enums.Gender;
import com.example.backend.enums.Shift;
import com.example.backend.model.Department;
import com.example.backend.model.Position;

public class EmployeeDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Gender gender;
    private Department department;
    private Position position;
    private Shift shift;
    private EmployeeStatus status;
    private LocalDate birthDate;
    private LocalDate hiredDate;
    private LocalDate terminationDate;

    

    public EmployeeDTO(Long id, String firstName, String lastName, String email, String phone, Gender gender,
            Department department, Position position, Shift shift, EmployeeStatus status, LocalDate birthDate,
            LocalDate hiredDate, LocalDate terminationDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.department = department;
        this.position = position;
        this.shift = shift;
        this.status = status;
        this.birthDate = birthDate;
        this.hiredDate = hiredDate;
        this.terminationDate = terminationDate;
    }

    public EmployeeDTO() {
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public Department getDepartment() {
        return department;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }
    public Shift getShift() {
        return shift;
    }
    public void setShift(Shift shift) {
        this.shift = shift;
    }
    public EmployeeStatus getStatus() {
        return status;
    }
    public void setStatus(EmployeeStatus status) {
        this.status = status;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public LocalDate getHiredDate() {
        return hiredDate;
    }
    public void setHiredDate(LocalDate hiredDate) {
        this.hiredDate = hiredDate;
    }
    public LocalDate getTerminationDate() {
        return terminationDate;
    }
    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    
    
}
