package com.example.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.example.backend.dto.AuthRequestDTO;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.CreateUserDTO;
import com.example.backend.dto.UpdatePasswordDTO;
import com.example.backend.dto.UserRespondeDTO;
import com.example.backend.enums.UserRole;
import com.example.backend.service.UserService;

import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired UserService userService;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM users WHERE id <> 1");
    }

    // generate token for login simulation
    private String getAuthToken(String username, String password) {
        AuthRequestDTO request = new AuthRequestDTO(username, password);
        ResponseEntity<AuthResponseDTO> response = restTemplate.postForEntity(
            "/auth", request, AuthResponseDTO.class);
        return response.getBody().getToken();
    }

    private UserRespondeDTO generateUser(String username, UserRole role) {
        
        CreateUserDTO createUserDTO = new CreateUserDTO(username, role);

        return userService.create(createUserDTO, "ADMIN");

    }

    @DisplayName("Test get users with admin role.")
    @Test
    void shouldReturnUsers_WhenUserHasAdminRole() {
        String token = getAuthToken("admin", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/users?page=0&size=12",
            HttpMethod.GET,
            entity,
            String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("Test get users with unauthorized role.")
    @Test
    void shouldReturnForbidden_WhenUserHasNoPermissionToListUsers() {
        generateUser("user", UserRole.HR);
        String token = getAuthToken("user", "user");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/users?page=0&size=12",
            HttpMethod.GET,
            entity,
            String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
    
    @Test
    void testCreate() {

    }

    @Test
    void testShow() {

    }

    @Test
    void testUpdate() {

    }

    @DisplayName("Test update password as admin.")
    @Test
    void updatePasswordWhenUserIsAdmin() {

        String token = getAuthToken("admin", "password");
        UserRespondeDTO user = generateUser("manager", UserRole.MANAGER);



        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("newPassword123");
        HttpEntity<UpdatePasswordDTO> entity = new HttpEntity<>(updatePasswordDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/users/" + user.getId(),
            HttpMethod.PATCH,
            entity,
            String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password updated successfully.", response.getBody());
    
    }

    @DisplayName("Test update password as the same user.")
    @Test
    void updatePasswordWhenUserIsUpdatingOwnPassword() {
        UserRespondeDTO user = generateUser("manager", UserRole.MANAGER);
        String token = getAuthToken("manager", "manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("newPassword123");
        HttpEntity<UpdatePasswordDTO> entity = new HttpEntity<>(updatePasswordDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/users/" + user.getId(),
            HttpMethod.PATCH,
            entity,
            String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password updated successfully.", response.getBody());
    }  

    @DisplayName("Test update password with unauthorized user.")
    @Test
    void shouldReturnForbidden_WhenUserIsNotAdminOrSelf() {
        generateUser("manager", UserRole.MANAGER);
        UserRespondeDTO userDeleted = generateUser("userDeleted", UserRole.ADMIN);
        String token = getAuthToken("manager", "manager");


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO("newPassword123");
        HttpEntity<UpdatePasswordDTO> entity = new HttpEntity<>(updatePasswordDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/users/" + userDeleted.getId(),
            HttpMethod.PATCH,
            entity,
            String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @DisplayName("Test delete user with admin role.")
    @Test
    void shouldDeleteUser_WhenUserHasAdminRole() {
        String token = getAuthToken("admin", "password");

        UserRespondeDTO user = generateUser("manager", UserRole.MANAGER);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/users/" + user.getId(),
            HttpMethod.DELETE,
            entity,
            String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The user has been deleted.", response.getBody());
    }

    @DisplayName("Test delete user with unauthorized role.")
    @Test
    void shouldReturnForbidden_WhenUserHasNoPermission() {
        Long userId = 1L;
        generateUser("breno", UserRole.HR);
        String token = getAuthToken("breno", "breno");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/users/" + userId,
            HttpMethod.DELETE,
            entity,
            String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


}
