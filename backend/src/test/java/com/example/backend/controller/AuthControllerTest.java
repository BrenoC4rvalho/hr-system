package com.example.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dto.AuthRequestDTO;
import com.example.backend.dto.AuthResponseDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @DisplayName("test login success.")
    @Test
    void testLogin() {
       
        AuthRequestDTO request = new AuthRequestDTO("admin", "password");

        ResponseEntity<AuthResponseDTO> response = restTemplate.postForEntity(
            "/auth", 
            request, 
            AuthResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getToken());

    }

    @DisplayName("test login failure with invalid credentials.")
    @Test
    void testLoginFailure() {

        AuthRequestDTO request = new AuthRequestDTO(
            "admin", 
            "admin"
        );

        ResponseEntity<?> response = restTemplate.postForEntity(
            "/auth",
            request,
            String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

}
