package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.backend.dto.UpdatePasswordDTO;
import com.example.backend.dto.UserRespondeDTO;
import com.example.backend.enums.UserRole;
import com.example.backend.enums.UserStatus;
import com.example.backend.exception.UserAlreadyInactiveException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.mapper.CreateUserMapper;
import com.example.backend.mapper.UserResponseMapper;
import com.example.backend.model.User;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.service.PasswordEncoderService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
   
    @Mock
    private UserRepository userRepository;

    @Mock 
    private EmployeeRepository employeeRepository;
    
    @Mock
    private PasswordEncoderService passwordEncoderService;   
    
    @Mock
    private CreateUserMapper createUserMapper;

    @Mock
    private UserResponseMapper userResponseMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRespondeDTO userResponseDTO;
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(UserRole.ADMIN);
        user.setStatus(UserStatus.ACTIVE);
        
        userResponseDTO = new UserRespondeDTO();
    }

    
    // Tests for method getUser

    @DisplayName("getUser: Should return user response DTO when user exists.")
    @Test
    void getUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userResponseMapper.map(user)).thenReturn(userResponseDTO);
        
        UserRespondeDTO result = userService.getUser(1L);
        
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userResponseMapper).map(user);

    }

    @DisplayName("getUser: Should throw exception when user not found.")
    @Test
    void getUserWhenNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));

    }

    // end getUser

    // Test for method getAllUser

    @Test
    @DisplayName("getAllUser: Should return page of user response DTO when users exist.")
    void testGetAll() {
        Page<User> users = new PageImpl<>(List.of(user));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(users);
        when(userResponseMapper.map(any(User.class))).thenReturn(userResponseDTO);
        
        Page<UserRespondeDTO> result = userService.getAll(PageRequest.of(0, 10));
        
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("getAllUser: Should throw exception when users not found")
    void testGetAllWhenUserNotFound() {
        Page<User> emptyPage = Page.empty();
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(emptyPage);
        
        assertThrows(UserNotFoundException.class, () -> userService.getAll(PageRequest.of(0, 10)));
    }

    // end getAllUser

    @Test
    void testCreate() {

    }

    @Test
    void testUpdate() {

    }

    //Tests for method updatePassword

    @DisplayName("updatePassword: Should update user password when user exists and active.")
    @Test
    void testUpdatePassword() {

        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoderService.encodePassword("newPassword")).thenReturn("hashedPassword");

        userService.updatePassword(1L, updatePasswordDTO);

        assertEquals("hashedPassword", user.getPasswordHash());
        verify(userRepository).save(user);
    
    }

    @DisplayName("updatePassword: Should throw exception when user not found.")
    @Test
    void testUpdatePasswordWhenUserNotFound() {

        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(1L, updatePasswordDTO));

    }

    @DisplayName("updatePassword: Should throw exception when user is inactive")
    @Test
    void testUpdatePasswordWhenUserInactive() {

        user.setStatus(UserStatus.INACTIVE);
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyInactiveException.class, () -> userService.updatePassword(1L, updatePasswordDTO));
        
    }
    // end updatePassword

    // Tests for method delete

    @Test
    @DisplayName("delete: Should delete user when user exists and active.")
    void testDelete() {
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        userService.delete(1L, "ADMIN");
        
        assertEquals(UserStatus.INACTIVE, user.getStatus());
        verify(userRepository).save(user);
    
    }

    @Test
    @DisplayName("delete: Should throw exception when user not found.")
    void testDeleteWhenUserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> userService.delete(1L, "ADMIN"));
    
    }

    // end delete

}
