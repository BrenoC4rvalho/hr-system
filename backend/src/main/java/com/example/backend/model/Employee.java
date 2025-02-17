package com.example.backend.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import com.example.backend.enums.EmployeeStatus;
import com.example.backend.enums.Gender;
import com.example.backend.enums.Shift;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Column(name = "first_name",nullable = false)
    @NotBlank
    @Length(min = 3, max = 100, message = "The first name field between 3 and 100 characters.")
    private String firstName;
    
    @Column(name = "last_name")
    @Length(max = 255, message = "The last name field cannot be greater than 255 characters.")
    private String lastName;

    @Email(message = "The email must be a valid format.")
    @Length(max = 255, message = "The email field cannot be greater than 255 characters.")
    private String email;
    
    @Column(nullable = false)
    @NotBlank(message = "The phone field cannot be blank.")
    @Length(max = 30, message = "The phone field cannot be greater than 30 characters.")
    private String phone;

    @NotNull(message = "The gender field cannot be null.")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull(message = "The department field cannot be null.")
    @ManyToOne()
    @JoinColumn(name = "department_id", referencedColumnName = "id", nullable = false)
    private Department department;
    
    @NotNull(message = "The position id field cannot be null.")
    @ManyToOne()
    @JoinColumn(name = "position_id", referencedColumnName = "id", nullable = false)
    private Position position;

    @NotNull(message = "The shift field cannot be null.")
    @Enumerated(EnumType.STRING)
    private Shift shift = Shift.NONE;

    @NotNull(message = "The status field cannot be null.")
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @Column(name = "birth_date", nullable = false)
    @NotNull(message = "The birth date field cannot be null.")
    private LocalDate birthDate;

    @Column(name = "hired_date")
    private LocalDate hiredDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;
    
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "manager")
    @JsonIgnore
    private List<Department> departments;

    @OneToOne(mappedBy = "employee")
    @JsonIgnore
    private User user;

    public Employee() {
    }

    public Employee(Long id, String firstName, String lastName, String email, String phone, Gender gender,
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
    
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
