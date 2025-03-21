package com.example.backend.controller;

import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

    @DisplayName("Test get all positions.")
    @Test
    void shouldReturnAllPositions() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<PositionDTO[]> response = restTemplate.exchange(
            "/positions", HttpMethod.GET, entity, PositionDTO[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @DisplayName("Test get position by id.")
    @Test
    void shouldReturnPositionById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<PositionDTO> response = restTemplate.exchange(
            "/positions/" + createdPositionId, HttpMethod.GET, entity, PositionDTO.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @DisplayName("Test update position.")
    @Test
    void shouldUpdatePosition() {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setName("edit");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PositionDTO> entity = new HttpEntity<>(positionDTO, headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            "/positions/" + createdPositionId, HttpMethod.PUT, entity, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Position updated successfully.", response.getBody().get("message"));
    }
}
