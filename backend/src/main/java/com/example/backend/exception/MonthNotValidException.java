package com.example.backend.exception;

public class MonthNotValidException extends RuntimeException  {
    public MonthNotValidException() {
        super("The month must be between 1 (January) and 12 (December).");
    }
}
