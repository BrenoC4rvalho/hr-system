package com.example.backend.exception;

public class UserAlreadyInactiveException extends RuntimeException {
    public UserAlreadyInactiveException() {
        super("User is already inactive.");
    }
}
