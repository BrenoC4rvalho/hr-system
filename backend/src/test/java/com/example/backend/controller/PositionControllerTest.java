package com.example.backend.controller;

import org.springframework.http.MediaType;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dto.AuthRequestDTO;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.CreateDepartmentDTO;
import com.example.backend.dto.CreatePositionDTO;
import com.example.backend.dto.DepartmentDTO;
import com.example.backend.dto.PositionDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PositionControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    private String authToken;
    private Long createdPositionId;

    private String getAuthToken(String username, String password) {
        AuthRequestDTO request = new AuthRequestDTO(username, password);
        ResponseEntity<AuthResponseDTO> response = restTemplate.postForEntity(
            "/auth", request, AuthResponseDTO.class);
        return response.getBody().getToken();
    }

    @BeforeEach
    void setUp() {
        authToken = getAuthToken("admin", "password");
        CreatePositionDTO request = new CreatePositionDTO("Manager", "Test description.");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreatePositionDTO> entity = new HttpEntity<>(request, headers);
        ResponseEntity<PositionDTO> response = restTemplate.postForEntity("/positions", entity,PositionDTO.class);
        createdPositionId = response.getBody().getId();
    }
}
