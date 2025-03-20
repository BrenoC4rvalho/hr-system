package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dto.AuthRequestDTO;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PositionControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired UserService userService;

    private String getAuthToken(String username, String password) {
        AuthRequestDTO request = new AuthRequestDTO(username, password);
        ResponseEntity<AuthResponseDTO> response = restTemplate.postForEntity(
            "/auth", request, AuthResponseDTO.class);
        return response.getBody().getToken();
    }
}
