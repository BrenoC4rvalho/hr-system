package com.example.backend.controller;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dto.AuthRequestDTO;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.CreateDepartmentDTO;
import com.example.backend.dto.DepartmentDTO;
import com.example.backend.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DepartmentControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired UserService userService;

    private String authToken;
    private Long createdDepartmentId;

    private String getAuthToken(String username, String password) {
        AuthRequestDTO request = new AuthRequestDTO(username, password);
        ResponseEntity<AuthResponseDTO> response = restTemplate.postForEntity(
            "/auth", request, AuthResponseDTO.class);
        return response.getBody().getToken();
    }

    @BeforeEach
    void setUp() {
        authToken = getAuthToken("admin", "password");
        CreateDepartmentDTO request = new CreateDepartmentDTO("HR");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateDepartmentDTO> entity = new HttpEntity<>(request, headers);
        ResponseEntity<DepartmentDTO> response = restTemplate.postForEntity("/departments", entity, DepartmentDTO.class);
        createdDepartmentId = response.getBody().getId();
    }

    @DisplayName("Test get all departments.")
    @Test
    void shouldReturnAllDepartments() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<DepartmentDTO[]> response = restTemplate.exchange(
            "/departments", HttpMethod.GET, entity, DepartmentDTO[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @DisplayName("Test create department.")
    @Test
    void shouldCreateDepartment() {

        String token = getAuthToken("admin", "password");

        CreateDepartmentDTO request = new CreateDepartmentDTO("HR");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateDepartmentDTO> entity = new HttpEntity<>(request, headers);
        
        ResponseEntity<DepartmentDTO> response = restTemplate.postForEntity("/departments", entity, DepartmentDTO.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @DisplayName("Test get department by id.")
    @Test
    void shouldReturnDepartmentById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<DepartmentDTO> response = restTemplate.exchange(
            "/departments/" + createdDepartmentId, HttpMethod.GET, entity, DepartmentDTO.class);
        
        System.out.println(response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @DisplayName("Test update department.")
    @Test
    void shouldUpdateDepartment() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("Updated Department");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DepartmentDTO> entity = new HttpEntity<>(departmentDTO, headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(
            "/departments/" + createdDepartmentId, HttpMethod.PUT, entity, Map.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Department updated successfully.", response.getBody().get("message"));
    }
}
