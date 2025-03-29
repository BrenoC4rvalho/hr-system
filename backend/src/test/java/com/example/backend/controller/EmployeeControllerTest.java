package com.example.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Map;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dto.AuthRequestDTO;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.CreateEmployeeDTO;
import com.example.backend.dto.EmployeeDTO;
import com.example.backend.enums.EmployeeStatus;
import com.example.backend.enums.Gender;
import com.example.backend.enums.Shift;
import com.example.backend.model.Department;
import com.example.backend.model.Employee;
import com.example.backend.model.Position;
import com.example.backend.repository.DepartmentRepository;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.PositionRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EmployeeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private String authToken;
    private HttpHeaders headers;
    private Employee testEmployee;

    private String getAuthToken(String username, String password) {
        AuthRequestDTO request = new AuthRequestDTO(username, password);
        ResponseEntity<AuthResponseDTO> response = restTemplate.postForEntity(
            "/auth", request, AuthResponseDTO.class);
        return response.getBody().getToken();
    }

    @BeforeEach
    void setUp() {
        this.cleanDatabase();
        authToken = getAuthToken("admin", "password");
        headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        testEmployee = createTestEmployee();
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM employees");
        jdbcTemplate.execute("DELETE FROM departments");
        jdbcTemplate.execute("DELETE FROM positions");
    }

    private Employee createTestEmployee() {
        Department department = new Department();
        department.setName("IT");
        department = departmentRepository.save(department);

        Position position = new Position();
        position.setName("Software Engineer");
        position = positionRepository.save(position);

        Employee employee = new Employee();
        employee.setFirstName("Test");
        employee.setLastName("User");
        employee.setEmail("test.user@example.com");
        employee.setPhone("123456789");
        employee.setGender(Gender.MALE);
        employee.setShift(Shift.MORNING);
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setBirthDate(LocalDate.of(1990, 1, 1));
        employee.setHiredDate(LocalDate.of(2020, 1, 1));
        employee.setDepartment(department);
        employee.setPosition(position);

        return employeeRepository.save(employee);
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
        createEmployeeDTO.setPhone("123456789");
        createEmployeeDTO.setDepartment(testEmployee.getDepartment());
        createEmployeeDTO.setPosition(testEmployee.getPosition());
        createEmployeeDTO.setShift(Shift.MORNING);
        createEmployeeDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        createEmployeeDTO.setHiredDate(LocalDate.of(2020, 1, 1));
        createEmployeeDTO.setGender(Gender.FEMALE);

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
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<EmployeeDTO> response = restTemplate.exchange(
            "/employees/" + testEmployee.getId(),
            HttpMethod.GET,
            requestEntity,
            EmployeeDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test", response.getBody().getFirstName());
    }

    @Test
    void shouldUpdateEmployee() {
        EmployeeDTO updateDTO = new EmployeeDTO();
        updateDTO.setFirstName("Updated");

        HttpEntity<EmployeeDTO> requestEntity = new HttpEntity<>(updateDTO, headers);

        ResponseEntity<EmployeeDTO> response = restTemplate.exchange(
            "/employees/" + testEmployee.getId(),
            HttpMethod.PUT,
            requestEntity,
            EmployeeDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated", response.getBody().getFirstName());
    }
}