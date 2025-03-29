package com.example.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dto.AuthRequestDTO;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.CreateDepartmentDTO;
import com.example.backend.dto.DepartmentDTO;
import com.example.backend.model.Department;
import com.example.backend.repository.DepartmentRepository;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DepartmentControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String authToken;
    private HttpHeaders headers;
    private Department testDepartment;

    private String getAuthToken(String username, String password) {
        AuthRequestDTO request = new AuthRequestDTO(username, password);
        ResponseEntity<AuthResponseDTO> response = restTemplate.postForEntity(
            "/auth", request, AuthResponseDTO.class);
        return response.getBody().getToken();
    }

    private void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM departments");
    }

    private Department createTestDepartment() {
        Department department = new Department();
        department.setName("HR");
        return departmentRepository.save(department);
    }

    @BeforeEach
    void setUp() {
        this.cleanDatabase();
        authToken = getAuthToken("admin", "password");
        headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        testDepartment = createTestDepartment();
    }

    @DisplayName("Test get all departments.")
    @Test
    void shouldReturnAllDepartments() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<DepartmentDTO[]> response = restTemplate.exchange(
            "/departments", HttpMethod.GET, requestEntity, DepartmentDTO[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @DisplayName("Test create department.")
    @Test
    void shouldCreateDepartment() {
        CreateDepartmentDTO createDepartmentDTO = new CreateDepartmentDTO("Finance");
        HttpEntity<CreateDepartmentDTO> requestEntity = new HttpEntity<>(createDepartmentDTO, headers);
        
        ResponseEntity<DepartmentDTO> response = restTemplate.postForEntity(
            "/departments", requestEntity, DepartmentDTO.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Finance", response.getBody().getName());
    }

    @DisplayName("Test get department by id.")
    @Test
    void shouldReturnDepartmentById() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<DepartmentDTO> response = restTemplate.exchange(
            "/departments/" + testDepartment.getId(), HttpMethod.GET, requestEntity, DepartmentDTO.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("HR", response.getBody().getName());
    }

    @DisplayName("Test update department.")
    @Test
    void shouldUpdateDepartment() {
        DepartmentDTO updateDTO = new DepartmentDTO();
        updateDTO.setName("Updated HR");

        HttpEntity<DepartmentDTO> requestEntity = new HttpEntity<>(updateDTO, headers);
        ResponseEntity<DepartmentDTO> response = restTemplate.exchange(
            "/departments/" + testDepartment.getId(), HttpMethod.PUT, requestEntity, DepartmentDTO.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}