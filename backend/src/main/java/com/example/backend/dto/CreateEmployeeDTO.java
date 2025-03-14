package com.example.backend.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.example.backend.enums.EmployeeStatus;
import com.example.backend.enums.Gender;
import com.example.backend.enums.Shift;
import com.example.backend.model.Department;
import com.example.backend.model.Position;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateEmployeeDTO {

    @NotBlank
    @Length(min = 3, max = 100, message = "The first name field between 3 and 100 characters.")
    private String firstName;

    @Length(max = 255, message = "The last name field cannot be greater than 255 characters.")
    private String lastName;

    @Email(message = "The email must be a valid format.")
    @Length(max = 255, message = "The email field cannot be greater than 255 characters.")
    private String email;
    
    @NotBlank(message = "The phone field cannot be blank.")
    @Length(max = 30, message = "The phone field cannot be greater than 30 characters.")
    private String phone;

    @NotNull(message = "The gender field cannot be null.")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull(message = "The department field cannot be null.")
    private Department department;
    
    @NotNull(message = "The position id field cannot be null.")
    private Position position;

    @Enumerated(EnumType.STRING)
    private Shift shift = Shift.NONE;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @NotNull(message = "The birth date field cannot be null.")
    private LocalDate birthDate;

    private LocalDate hiredDate;

    public CreateEmployeeDTO(
            @NotBlank @Length(min = 3, max = 100, message = "The first name field between 3 and 100 characters.") String firstName,
            @Length(max = 255, message = "The last name field cannot be greater than 255 characters.") String lastName,
            @Email(message = "The email must be a valid format.") @Length(max = 255, message = "The email field cannot be greater than 255 characters.") String email,
            @NotBlank(message = "The phone field cannot be blank.") @Length(max = 30, message = "The phone field cannot be greater than 30 characters.") String phone,
            @NotNull(message = "The gender field cannot be null.") Gender gender,
            @NotNull(message = "The department field cannot be null.") Department department,
            @NotNull(message = "The position id field cannot be null.") Position position, Shift shift,
            EmployeeStatus status, @NotNull(message = "The birth date field cannot be null.") LocalDate birthDate,
            LocalDate hiredDate
    ) {
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
    }

    public CreateEmployeeDTO() {
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

    
}