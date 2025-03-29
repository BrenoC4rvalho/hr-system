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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dto.AuthRequestDTO;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.CreatePositionDTO;
import com.example.backend.dto.PositionDTO;
import com.example.backend.model.Position;
import com.example.backend.repository.PositionRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PositionControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PositionRepository positionRepository;

    private String authToken;
    private HttpHeaders headers;
    private Position testPosition;

    private String getAuthToken(String username, String password) {
        AuthRequestDTO request = new AuthRequestDTO(username, password);
        ResponseEntity<AuthResponseDTO> response = restTemplate.postForEntity(
            "/auth", request, AuthResponseDTO.class);
        return response.getBody().getToken();
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM positions");
    }

    private Position createTestPosition() {
        Position position = new Position();
        position.setName("Manager");
        position.setDescription("Test description.");
        return positionRepository.save(position);
    }

    @BeforeEach
    void setUp() {
        this.cleanDatabase();
        authToken = getAuthToken("admin", "password");
        headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        testPosition = createTestPosition();
    }

    @DisplayName("Test get all positions.")
    @Test
    void shouldReturnAllPositions() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<PositionDTO[]> response = restTemplate.exchange(
            "/positions", HttpMethod.GET, requestEntity, PositionDTO[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @DisplayName("Test create position.")
    @Test
    void shouldCreatePosition() {
        CreatePositionDTO request = new CreatePositionDTO("Supervisor", "Another test description.");
        HttpEntity<CreatePositionDTO> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<PositionDTO> response = restTemplate.postForEntity(
            "/positions", requestEntity, PositionDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @DisplayName("Test get position by id.")
    @Test
    void shouldReturnPositionById() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<PositionDTO> response = restTemplate.exchange(
            "/positions/" + testPosition.getId(), HttpMethod.GET, requestEntity, PositionDTO.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @DisplayName("Test update position.")
    @Test
    void shouldUpdatePosition() {
        PositionDTO updateDTO = new PositionDTO();
        updateDTO.setName("Updated Position");
        HttpEntity<PositionDTO> requestEntity = new HttpEntity<>(updateDTO, headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            "/positions/" + testPosition.getId(), HttpMethod.PUT, requestEntity, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Position updated successfully.", response.getBody().get("message"));
    }
}