package com.example.backend.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.dto.AuthRequestDTO;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.enums.UserRole;
import com.example.backend.enums.UserStatus;
import com.example.backend.exception.AuthFailedException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderService passwordEncoderService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    @DisplayName("shoul authenticate and return token for valid credentials")
    @Test
    void login() {

        String username = "testuser";
        String password = "password123";
        String hashedPassword = "hashedPassword";
        Long userId = 1L;
        UserRole userRole = UserRole.ADMIN;
        String generatedToken = "generatedToken";

        AuthRequestDTO authRequestDTO = new AuthRequestDTO(username, password);
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setPasswordHash(hashedPassword);
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(userRole);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoderService.matchPassword(password, hashedPassword)).thenReturn(true);
        when(tokenService.generateToken(userId, userRole)).thenReturn(generatedToken);

        AuthResponseDTO response = authService.login(authRequestDTO);

        assertNotNull(response);
        assertEquals(generatedToken, response.getToken());

        verify(userRepository).findByUsername(username);
        verify(passwordEncoderService).matchPassword(password, hashedPassword);
        verify(tokenService).generateToken(userId, userRole);
    }


    @DisplayName("should throw AuthFailedException when user is not found in the repository")
    @Test
    void loginWithNonExistentUser() {
        String username = "nonexistentuser";
        String password = "password123";
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(AuthFailedException.class, () -> authService.login(authRequestDTO));

        verify(userRepository).findByUsername(username);
    }

    @DisplayName("should throw AuthFailedException when user status is INACTIVE")
    @Test
    void loginWithInactiveUser() {
        String username = "inactiveuser";
        String password = "password123";
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(username, password);

        User inactiveUser = new User();
        inactiveUser.setUsername(username);
        inactiveUser.setStatus(UserStatus.INACTIVE);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(inactiveUser));

        assertThrows(AuthFailedException.class, () -> authService.login(authRequestDTO));

        verify(userRepository).findByUsername(username);
    }

    @DisplayName("should throw AuthFailedException when password doesn't match")
    @Test
    void loginWithIncorrectPassword() {
        String username = "testuser";
        String password = "incorrectPassword";
        String hashedPassword = "correctHashedPassword";
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(username, password);

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hashedPassword);
        user.setStatus(UserStatus.ACTIVE);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoderService.matchPassword(password, hashedPassword)).thenReturn(false);

        assertThrows(AuthFailedException.class, () -> authService.login(authRequestDTO));

        verify(userRepository).findByUsername(username);
        verify(passwordEncoderService).matchPassword(password, hashedPassword);
    }

    @DisplayName("should throw AuthFailedException when username is empty")
    @Test
    void loginWithEmptyUsername() {
        String emptyUsername = "";
        String password = "password123";
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(emptyUsername, password);

        when(userRepository.findByUsername(emptyUsername)).thenReturn(Optional.empty());

        assertThrows(AuthFailedException.class, () -> authService.login(authRequestDTO));

        verify(userRepository).findByUsername(emptyUsername);
    }

    @DisplayName("should throw AuthFailedException when password is empty")
    @Test
    void loginWithEmptyPassword() {
        String username = "testuser";
        String emptyPassword = "";
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(username, emptyPassword);

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash("hashedPassword");
        user.setStatus(UserStatus.ACTIVE);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoderService.matchPassword(emptyPassword, user.getPasswordHash())).thenReturn(false);

        assertThrows(AuthFailedException.class, () -> authService.login(authRequestDTO));

        verify(userRepository).findByUsername(username);
        verify(passwordEncoderService).matchPassword(emptyPassword, user.getPasswordHash());
    }
}
