package com.example.backend.model;

import java.time.LocalDate;

import com.example.backend.Enum.EmployeeStatus;
import com.example.backend.Enum.Gender;
import com.example.backend.Enum.Shift;

public class Employee {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Gender gender;
    private int departmentId;
    private int positionId;
    private Shift shift;
    private EmployeeStatus status = EmployeeStatus.ACTIVE;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private LocalDate terminationDate;
    
    public Employee() {
    }

    public Employee(int id, String firstName, String lastName, String email, String phone, Gender gender,
            int departmentId, int positionId, Shift shift, EmployeeStatus status, LocalDate birthDate,
            LocalDate hireDate, LocalDate terminationDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.departmentId = departmentId;
        this.positionId = positionId;
        this.shift = shift;
        this.status = status;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.terminationDate = terminationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
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

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
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

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public LocalDate getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }
    
}
