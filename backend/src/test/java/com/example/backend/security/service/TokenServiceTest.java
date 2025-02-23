package com.example.backend.security.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.backend.enums.UserRole;
import com.example.backend.security.provider.TokenProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private TokenProvider jwtProvider;

    @InjectMocks
    private TokenService tokenService;

    private final Long userId = 1L;
    private final UserRole userRole = UserRole.ADMIN;
    private final String fakeToken = "fake.jwt.token";
    private final Algorithm mockAlgorithm = Algorithm.HMAC256("secret");

    @BeforeEach
    void setUp() {
        lenient().when(jwtProvider.getAlgorithm()).thenReturn(mockAlgorithm);
        lenient().when(jwtProvider.getIssuer()).thenReturn("test-issuer");
        lenient().when(jwtProvider.getCreationDate()).thenReturn(new Date().toInstant());
        lenient().when(jwtProvider.getExpirationDate()).thenReturn(new Date(System.currentTimeMillis() + 60000).toInstant()); // +1 min
    }

    @Test
    void shouldGenerateValidToken() {
        String token = tokenService.generateToken(userId, userRole);

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // Token JWT possui 3 partes (Header, Payload, Signature)

        verify(jwtProvider, times(1)).getAlgorithm();
        verify(jwtProvider, times(1)).getIssuer();
        verify(jwtProvider, times(1)).getCreationDate();
        verify(jwtProvider, times(1)).getExpirationDate();
    }

    @Test
    void shouldThrowExceptionWhenGeneratingTokenWithInvalidData() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> tokenService.generateToken(null, userRole));
        assertEquals("Invalid token payload.", exception1.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> tokenService.generateToken(userId, null));
        assertEquals("Invalid token payload.", exception2.getMessage());
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        String validToken = tokenService.generateToken(userId, userRole);
    
        DecodedJWT decodedJWT = tokenService.validateToken(validToken);
    
        assertNotNull(decodedJWT);
        assertEquals(userId.toString(), decodedJWT.getSubject());
        assertEquals(userRole.toString(), decodedJWT.getClaim("role").asString());
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        Exception exception = assertThrows(JWTVerificationException.class, () -> tokenService.validateToken("invalid.token"));

        assertEquals("Expired token or Invalid.", exception.getMessage());
    }

}
