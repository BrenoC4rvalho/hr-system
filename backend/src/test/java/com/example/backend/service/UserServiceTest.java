package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.dto.UserRespondeDTO;
import com.example.backend.enums.UserRole;
import com.example.backend.enums.UserStatus;
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
    void getUserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));

    }

    // end getUser

    // Test for method getAllUser


    @Test
    void testGetAll() {

    }

    @Test
    void testCreate() {

    }

    @Test
    void testUpdate() {

    }

    @Test
    void testUpdatePassword() {

    }


    @Test
    void testDelete() {

    }

}
