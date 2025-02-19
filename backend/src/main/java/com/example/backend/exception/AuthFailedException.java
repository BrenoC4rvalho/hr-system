package com.example.backend.exception;

public class AuthFailedException extends RuntimeException {
    public AuthFailedException() {
        super("The username or password is incorrect.");
    }
}