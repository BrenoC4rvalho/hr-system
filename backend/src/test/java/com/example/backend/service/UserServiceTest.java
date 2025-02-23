package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import org.springframework.security.authorization.AuthorizationDeniedException;

import com.example.backend.dto.CreateUserDTO;
import com.example.backend.dto.UpdatePasswordDTO;
import com.example.backend.dto.UpdateUserDTO;
import com.example.backend.dto.UserRespondeDTO;
import com.example.backend.enums.UserRole;
import com.example.backend.enums.UserStatus;
import com.example.backend.exception.EmployeeNotFoundException;
import com.example.backend.exception.UserAlreadyInactiveException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.mapper.CreateUserMapper;
import com.example.backend.mapper.UserResponseMapper;
import com.example.backend.model.Employee;
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

    // Test for method createUser

    @Test
    @DisplayName("create: Should create a user successfully when all input is valid and creator is admin")
    void testCreateUserSuccessfullyAsAdmin() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("newuser");
        createUserDTO.setRole(UserRole.MANAGER);
        createUserDTO.setEmployeeId(1L);

        Employee employee = new Employee();
        employee.setId(1L);

        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setRole(UserRole.MANAGER);
        newUser.setEmployee(employee);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        savedUser.setRole(UserRole.MANAGER);
        savedUser.setEmployee(employee);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmployeeId(1L)).thenReturn(false);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(createUserMapper.map(createUserDTO)).thenReturn(newUser);
        when(passwordEncoderService.encodePassword("newuser")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userResponseMapper.map(savedUser)).thenReturn(new UserRespondeDTO());

        UserRespondeDTO result = userService.create(createUserDTO, "ADMIN");

        assertNotNull(result);
        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmployeeId(1L);
        verify(employeeRepository).findById(1L);
        verify(createUserMapper).map(createUserDTO);
        verify(passwordEncoderService).encodePassword("newuser");
        verify(userRepository).save(newUser);
        verify(userResponseMapper).map(savedUser);
    }
    
    @Test
    @DisplayName("create: Should throw IllegalArgumentException when creating a user with an existing username")
    void testCreateUserWithExistingUsername() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("existingUsername");
        createUserDTO.setRole(UserRole.MANAGER);
    
        when(userRepository.existsByUsername("existingUsername")).thenReturn(true);
    
        assertThrows(IllegalArgumentException.class, () -> 
            userService.create(createUserDTO, "ADMIN"),
            "Username already exists."
        );
    
        verify(userRepository).existsByUsername("existingUsername");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("create: Should throw AuthorizationDeniedException when non-admin tries to create an admin user")
    void testCreateAdminUserByNonAdmin() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setRole(UserRole.ADMIN);
        createUserDTO.setUsername("newadmin");

        assertThrows(AuthorizationDeniedException.class, () -> 
            userService.create(createUserDTO, "MANAGER"),
            "You don't have permission to create an admin user."
        );

        verify(userRepository, never()).existsByUsername(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("create: Should throw IllegalArgumentException when creating a user for an employee who already has a user")
    void testCreateUserForEmployeeWithExistingUser() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("newuser");
        createUserDTO.setRole(UserRole.MANAGER);
        createUserDTO.setEmployeeId(1L);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmployeeId(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> 
            userService.create(createUserDTO, "ADMIN"),
            "Employee already has a user."
        );

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmployeeId(1L);
        verify(employeeRepository, never()).findById(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("create: Should create a non-admin user successfully when creator is non-admin")
    void testCreateNonAdminUserByNonAdmin() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("newuser");
        createUserDTO.setRole(UserRole.MANAGER);
        createUserDTO.setEmployeeId(1L);

        Employee employee = new Employee();
        employee.setId(1L);

        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setRole(UserRole.MANAGER);
        newUser.setEmployee(employee);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        savedUser.setRole(UserRole.MANAGER);
        savedUser.setEmployee(employee);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmployeeId(1L)).thenReturn(false);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(createUserMapper.map(createUserDTO)).thenReturn(newUser);
        when(passwordEncoderService.encodePassword("newuser")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userResponseMapper.map(savedUser)).thenReturn(new UserRespondeDTO());

        UserRespondeDTO result = userService.create(createUserDTO, "MANAGER");

        assertNotNull(result);
        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmployeeId(1L);
        verify(employeeRepository).findById(1L);
        verify(createUserMapper).map(createUserDTO);
        verify(passwordEncoderService).encodePassword("newuser");
        verify(userRepository).save(newUser);
        verify(userResponseMapper).map(savedUser);
    }

    @Test
    @DisplayName("create: Should throw EmployeeNotFoundException when creating a user with non-existent employeeId")
    void testCreateUserWithNonExistentEmployeeId() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("newuser");
        createUserDTO.setRole(UserRole.MANAGER);
        createUserDTO.setEmployeeId(999L); // Non-existent employee ID

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmployeeId(999L)).thenReturn(false);
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> 
            userService.create(createUserDTO, "ADMIN")
        );

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmployeeId(999L);
        verify(employeeRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("create: Should create a user without an associated employee when employeeId is null")
    void testCreateUserWithoutEmployee() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("newuser");
        createUserDTO.setRole(UserRole.MANAGER);
        createUserDTO.setEmployeeId(null);

        User newUser = new User();
        newUser.setId(1L);
        newUser.setUsername("newuser");
        newUser.setRole(UserRole.MANAGER);

        UserRespondeDTO userResponseDTO = new UserRespondeDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setUsername("newuser");
        userResponseDTO.setRole(UserRole.MANAGER);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(createUserMapper.map(createUserDTO)).thenReturn(newUser);
        when(passwordEncoderService.encodePassword("newuser")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(userResponseMapper.map(newUser)).thenReturn(userResponseDTO);

        UserRespondeDTO result = userService.create(createUserDTO, "ADMIN");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("newuser", result.getUsername());
        assertEquals(UserRole.MANAGER, result.getRole());

        verify(userRepository).existsByUsername("newuser");
        verify(createUserMapper).map(createUserDTO);
        verify(passwordEncoderService).encodePassword("newuser");
        verify(userRepository).save(newUser);
        verify(userResponseMapper).map(newUser);
        verify(employeeRepository, never()).findById(any());
    }


    @Test
    @DisplayName("create: Should set the password hash correctly using the passwordEncoderService")
    void testCreateUserSetsPasswordHashCorrectly() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("newuser");
        createUserDTO.setRole(UserRole.MANAGER);

        User newUser = new User();
        newUser.setUsername("newuser");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(createUserMapper.map(createUserDTO)).thenReturn(newUser);
        when(passwordEncoderService.encodePassword("newuser")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(userResponseMapper.map(newUser)).thenReturn(new UserRespondeDTO());

        userService.create(createUserDTO, "ADMIN");

        assertEquals("hashedPassword", newUser.getPasswordHash());
        verify(passwordEncoderService).encodePassword("newuser");
        verify(userRepository).save(newUser);
    }

    @Test
    @DisplayName("create: Should return the correct UserRespondeDTO after successful user creation")
    void testSuccessfulUserCreation() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("newuser");
        createUserDTO.setRole(UserRole.MANAGER);
        createUserDTO.setEmployeeId(1L);

        Employee employee = new Employee();
        employee.setId(1L);

        User newUser = new User();
        newUser.setId(1L);
        newUser.setUsername("newuser");
        newUser.setRole(UserRole.MANAGER);
        newUser.setEmployee(employee);

        UserRespondeDTO expectedUserResponseDTO = new UserRespondeDTO();
        expectedUserResponseDTO.setId(1L);
        expectedUserResponseDTO.setUsername("newuser");
        expectedUserResponseDTO.setRole(UserRole.MANAGER);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmployeeId(1L)).thenReturn(false);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(createUserMapper.map(createUserDTO)).thenReturn(newUser);
        when(passwordEncoderService.encodePassword("newuser")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(userResponseMapper.map(newUser)).thenReturn(expectedUserResponseDTO);

        UserRespondeDTO result = userService.create(createUserDTO, "ADMIN");

        assertNotNull(result);
        assertEquals(expectedUserResponseDTO.getId(), result.getId());
        assertEquals(expectedUserResponseDTO.getUsername(), result.getUsername());
        assertEquals(expectedUserResponseDTO.getRole(), result.getRole());

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmployeeId(1L);
        verify(employeeRepository).findById(1L);
        verify(createUserMapper).map(createUserDTO);
        verify(passwordEncoderService).encodePassword("newuser");
        verify(userRepository).save(newUser);
        verify(userResponseMapper).map(newUser);
    }

    //end create

    // Tests for method update

    @Test
    @DisplayName("update: Should successfully update user when all provided fields are valid and user is an admin")
    void testSuccessfulUpdateByAdmin() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setRole(UserRole.MANAGER);
        existingUser.setStatus(UserStatus.INACTIVE);

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("newUsername");
        updateUserDTO.setRole(UserRole.ADMIN);
        updateUserDTO.setStatus(UserStatus.ACTIVE);
        updateUserDTO.setEmployeeId(2L);

        Employee employee = new Employee();
        employee.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("newUsername")).thenReturn(false);
        when(userRepository.existsByEmployeeId(2L)).thenReturn(false);
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userResponseMapper.map(any(User.class))).thenReturn(new UserRespondeDTO());

        UserRespondeDTO result = userService.update(1L, updateUserDTO, "ADMIN");

        assertNotNull(result);
        assertEquals("newUsername", existingUser.getUsername());
        assertEquals(UserRole.ADMIN, existingUser.getRole());
        assertEquals(UserStatus.ACTIVE, existingUser.getStatus());
        assertEquals(employee, existingUser.getEmployee());

        verify(userRepository).findById(1L);
        verify(userRepository).existsByUsername("newUsername");
        verify(userRepository).existsByEmployeeId(2L);
        verify(employeeRepository).findById(2L);
        verify(userRepository).save(existingUser);
        verify(userResponseMapper).map(existingUser);
    }

    @Test
    @DisplayName("update: Should throw AuthorizationDeniedException when non-admin user tries to update an admin user")
    void testUpdateAdminUserByNonAdmin() {
        
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setRole(UserRole.ADMIN);

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("newUsername");

        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));

        assertThrows(AuthorizationDeniedException.class, () -> 
            userService.update(1L, updateUserDTO, "MANAGER")
        );

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("update: Should throw IllegalArgumentException when new username is less than 3 characters")
    void testUpdateWithShortUsername() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setRole(UserRole.ADMIN);

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("ab");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> 
            userService.update(1L, updateUserDTO, "ADMIN"),
            "The username field between 3 and 50 characters."
        );

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("update: Should throw IllegalArgumentException when new username is more than 50 characters")
    void testUpdateWithLongUsername() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setRole(UserRole.ADMIN);

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setUsername("a".repeat(51)); // Creates a string with 51 'a' characters

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> 
            userService.update(1L, updateUserDTO, "ADMIN"),
            "The username field between 3 and 50 characters."
        );

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("update: Should throw AuthorizationDeniedException when manager tries to change role to ADMIN")
    void testUpdateRoleToAdminByManager() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setRole(UserRole.MANAGER);

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setRole(UserRole.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        assertThrows(AuthorizationDeniedException.class, () -> 
            userService.update(1L, updateUserDTO, "MANAGER"),
            "Manager can't change role to ADMIN."
        );

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("update: Should throw IllegalArgumentException when trying to link user to an employee who already has a user")
    void testUpdateWithEmployeeAlreadyLinkedToUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("oldUsername");
        existingUser.setRole(UserRole.MANAGER);

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setEmployeeId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmployeeId(2L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> 
            userService.update(1L, updateUserDTO, "ADMIN"),
            "Employee already has a user."
        );

        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmployeeId(2L);
        verify(userRepository, never()).save(any(User.class));
        verify(employeeRepository, never()).findById(any());
    }

    // end update

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
