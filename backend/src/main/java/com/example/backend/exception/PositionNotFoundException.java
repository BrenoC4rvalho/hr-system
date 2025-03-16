package com.example.backend.exception;

public class PositionNotFoundException extends RuntimeException {

    public PositionNotFoundException() {
        super("Position not found.");
    }
    
}
