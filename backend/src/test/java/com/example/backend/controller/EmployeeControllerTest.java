package com.example.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import com.example.backend.dto.CreateEmployeeDTO;
import com.example.backend.dto.EmployeeDTO;
import com.example.backend.model.Employee;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EmployeeControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService; 

    @Autowired
    private EmployeeRepository employeeRepository;

    private String authToken;
    private HttpHeaders headers;

    private String getAuthToken(String username, String password) {
        AuthRequestDTO request = new AuthRequestDTO(username, password);
        ResponseEntity<AuthResponseDTO> response = restTemplate.postForEntity(
            "/auth", request, AuthResponseDTO.class);
        return response.getBody().getToken();
    }

    @BeforeAll
    void setUp() {
        authToken = getAuthToken("admin", "password");

        headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldReturnAllEmployees() {
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
            "/employees?page=0&size=5",
            HttpMethod.GET,
            requestEntity,
            Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("employees"));
    }

    @Test
    void shouldCreateEmployee() {
        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO();
        createEmployeeDTO.setFirstName("John");
        createEmployeeDTO.setLastName("Doe");
        createEmployeeDTO.setEmail("john.doe@example.com");

        HttpEntity<CreateEmployeeDTO> requestEntity = new HttpEntity<>(createEmployeeDTO, headers);

        ResponseEntity<EmployeeDTO> response = restTemplate.exchange(
            "/employees",
            HttpMethod.POST,
            requestEntity,
            EmployeeDTO.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
    }

    @Test
    void shouldReturnEmployeeById() {
        Employee employee = new Employee();
        employee.setFirstName("Jane");
        employee.setLastName("Smith");
        employee.setEmail("jane.smith@example.com");
        employee = employeeRepository.save(employee);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<EmployeeDTO> response = restTemplate.exchange(
            "/employees/" + employee.getId(),
            HttpMethod.GET,
            requestEntity,
            EmployeeDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jane", response.getBody().getFirstName());
    }

    @Test
    void shouldUpdateEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("Mike");
        employee.setLastName("Brown");
        employee.setEmail("mike.brown@example.com");
        employee = employeeRepository.save(employee);

        EmployeeDTO updateDTO = new EmployeeDTO();
        updateDTO.setFirstName("Michael");

        HttpEntity<EmployeeDTO> requestEntity = new HttpEntity<>(updateDTO, headers);

        ResponseEntity<EmployeeDTO> response = restTemplate.exchange(
            "/employees/" + employee.getId(),
            HttpMethod.PUT,
            requestEntity,
            EmployeeDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Michael", response.getBody().getFirstName());
    }


}
