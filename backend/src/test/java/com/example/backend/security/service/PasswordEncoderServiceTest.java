package com.example.backend.security.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordEncoderService passwordEncoderService;

    private final String rawPassword = "password123";
    private final String encodedPassword = "$2a$10$123456789012345678901234567890123456789012345678901234";

    @BeforeEach
    void setUp() {
        // Use lenient para evitar erro de UnnecessaryStubbingException
        lenient().when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        lenient().when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
    }

    @Test
    void shouldEncodePassword() {
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        String result = passwordEncoderService.encodePassword(rawPassword);

        assertEquals(encodedPassword, result);
        verify(passwordEncoder, times(1)).encode(rawPassword);
    }

    @Test
    void shouldMatchPassword() {
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = passwordEncoderService.matchPassword(rawPassword, encodedPassword);

        assertTrue(result);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }
}
