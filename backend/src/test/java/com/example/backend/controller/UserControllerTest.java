package com.example.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.example.backend.dto.UpdateUserDTO;
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
    
    @DisplayName("Should return 201 Created status code upon successful user creation")
    @Test
    void shouldReturnCreatedStatusCodeWhenUserIsCreatedSuccessfully() {
        String token = getAuthToken("admin", "password");
        CreateUserDTO createUserDTO = new CreateUserDTO("newuser", UserRole.HR);
    
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateUserDTO> entity = new HttpEntity<>(createUserDTO, headers);
    
        ResponseEntity<UserRespondeDTO> response = restTemplate.exchange(
            "/users",
            HttpMethod.POST,
            entity,
            UserRespondeDTO.class
        );
    
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newuser", response.getBody().getUsername());
        assertEquals(UserRole.HR, response.getBody().getRole());
    }

    @DisplayName("Should return 400 Bad Request when creating a user with invalid input data")
    @Test
    void shouldReturnBadRequestWhenCreatingUserWithInvalidData() {
        String token = getAuthToken("admin", "password");
        CreateUserDTO invalidUserDTO = new CreateUserDTO("", null);  // Invalid username and role

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateUserDTO> entity = new HttpEntity<>(invalidUserDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/users",
            HttpMethod.POST,
            entity,
            String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @DisplayName("Should return 403 Forbidden when a non-ADMIN or non-MANAGER attempts to create a user")
    @Test
    void shouldReturnForbiddenWhenNonAdminOrNonManagerAttemptsToCreateUser() {
        UserRespondeDTO user = generateUser("hruser", UserRole.HR);
        String token = getAuthToken("hruser", "hruser");

        CreateUserDTO createUserDTO = new CreateUserDTO("newuser", UserRole.HR);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateUserDTO> entity = new HttpEntity<>(createUserDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/users",
            HttpMethod.POST,
            entity,
            String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
    

    @DisplayName("Should return 200 OK when an ADMIN requests a user by ID")
    @Test
    void shouldReturnOkWhenAdminRequestsUserById() {
        String token = getAuthToken("admin", "password");
        UserRespondeDTO user = generateUser("testuser", UserRole.HR);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserRespondeDTO> response = restTemplate.exchange(
            "/users/" + user.getId(),
            HttpMethod.GET,
            entity,
            UserRespondeDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getId(), response.getBody().getId());
        assertEquals(user.getUsername(), response.getBody().getUsername());
        assertEquals(user.getRole(), response.getBody().getRole());
    }

    @DisplayName("Should return 200 OK when a user requests their own information by ID")
    @Test
    void shouldReturnOkWhenUserRequestsOwnInformation() {
        UserRespondeDTO user = generateUser("testuser", UserRole.HR);
        String token = getAuthToken("testuser", "testuser");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserRespondeDTO> response = restTemplate.exchange(
            "/users/" + user.getId(),
            HttpMethod.GET,
            entity,
            UserRespondeDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user.getId(), response.getBody().getId());
        assertEquals(user.getUsername(), response.getBody().getUsername());
        assertEquals(user.getRole(), response.getBody().getRole());
    }

    @DisplayName("Should return 403 Forbidden when a non-ADMIN/MANAGER user requests another user's information")
    @Test
    void shouldReturnForbiddenWhenNonAdminManagerRequestsOtherUserInfo() {
        UserRespondeDTO user1 = generateUser("user1", UserRole.HR);
        UserRespondeDTO user2 = generateUser("user2", UserRole.HR);
        String token = getAuthToken("user1", "user1");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/users/" + user2.getId(),
            HttpMethod.GET,
            entity,
            String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }



    @DisplayName("Should return 200 OK when an ADMIN successfully updates another user's information")
    @Test
    void shouldReturnOkWhenAdminUpdatesAnotherUserInfo() {
        String token = getAuthToken("admin", "password");
        UserRespondeDTO user = generateUser("testuser", UserRole.HR);
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("updatedUser");
        updateUserDTO.setRole(UserRole.MANAGER);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UpdateUserDTO> entity = new HttpEntity<>(updateUserDTO, headers);

        ResponseEntity<UserRespondeDTO> response = restTemplate.exchange(
            "/users/" + user.getId(),
            HttpMethod.PUT,
            entity,
            UserRespondeDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("updatedUser", response.getBody().getUsername());
        assertEquals(UserRole.MANAGER, response.getBody().getRole());
    }

    @DisplayName("Should return 404 Not Found when updating a non-existent user")
    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentUser() {
        String token = getAuthToken("admin", "password");
        Long nonExistentUserId = 9999L;
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("updatedUsername");
        updateUserDTO.setRole(UserRole.HR);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UpdateUserDTO> entity = new HttpEntity<>(updateUserDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/users/" + nonExistentUserId,
            HttpMethod.PUT,
            entity,
            String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @DisplayName("Should return 403 Forbidden when a non-ADMIN user attempts to update another user's information")
    @Test
    void shouldReturnForbiddenWhenNonAdminAttemptsToUpdateOtherUserInfo() {
        UserRespondeDTO user1 = generateUser("user1", UserRole.HR);
        UserRespondeDTO user2 = generateUser("user2", UserRole.HR);
        String token = getAuthToken("user1", "user1");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("updatedUsername");
        updateUserDTO.setRole(UserRole.MANAGER);

        HttpEntity<UpdateUserDTO> entity = new HttpEntity<>(updateUserDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/users/" + user2.getId(),
            HttpMethod.PUT,
            entity,
            String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
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

    @DisplayName("Test get current user with jwt authorization")
    @Test
    void getCurrentUserWithJwtAuthorization() {
        
        String token = getAuthToken("admin", "password");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserRespondeDTO> response = restTemplate.exchange(
            "/users/me",
            HttpMethod.GET,
            entity,
            UserRespondeDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());


    }


}
