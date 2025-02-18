package com.example.backend.security.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderService {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matchPassword(String loginPassword, String encodedPassword) {
        return passwordEncoder.matches(loginPassword, encodedPassword);
    }

}
