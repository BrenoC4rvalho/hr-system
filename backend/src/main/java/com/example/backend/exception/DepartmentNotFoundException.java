package com.example.backend.exception;

public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException() {
        super("Department not found.");
    }
}
