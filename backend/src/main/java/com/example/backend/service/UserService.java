package com.example.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.enums.UserRole;
import com.example.backend.enums.UserStatus;
import com.example.backend.dto.CreateUserDTO;
import com.example.backend.dto.UserRespondeDTO;
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

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    
    private final PasswordEncoderService passwordEncoderService;
    
    private final CreateUserMapper createUserMapper;
    private final UserResponseMapper userResponseMapper;

    public UserService(
        UserRepository userRepository, 
        EmployeeRepository employeeRepository, 
        CreateUserMapper createUserMapper,
        UserResponseMapper userResponseMapper, 
        PasswordEncoderService passwordEncoderService
    ) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.createUserMapper = createUserMapper;
        this.userResponseMapper = userResponseMapper;
        this.passwordEncoderService = passwordEncoderService;
    }


    public UserRespondeDTO getUser(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        return userResponseMapper.map(user);

    }

    public Page<UserRespondeDTO> getAll(Pageable pageable) {

        Page<User> users = userRepository.findAll(pageable);

        if(users.isEmpty()) {
            throw new UserNotFoundException();
        }

        return users.map(userResponseMapper::map);

    }

    @Transactional
    public UserRespondeDTO create(CreateUserDTO createUserDTO, String roleOfCreatorUser) {
        
        boolean isCreatorUserAdmin = "ADMIN".equals(roleOfCreatorUser); 
        boolean isNewUserAdmin = createUserDTO.getRole().equals(UserRole.ADMIN);
        if(isNewUserAdmin && !isCreatorUserAdmin) {
            throw new AuthorizationDeniedException("You don't have permission to create an admin user.");
        }

        Boolean existsUserWithUsername = userRepository.existsByUsername(createUserDTO.getUsername());
        if (existsUserWithUsername) {
            throw new  IllegalArgumentException("Username already exists.");
        }
            
        Boolean existsUserRelateEmployee = createUserDTO.getEmployeeId() != null ? userRepository.existsByEmployeeId(createUserDTO.getEmployeeId()) : false;
        if(existsUserRelateEmployee) {
            throw new IllegalArgumentException("Employee already has a user.");
        }

        if(createUserDTO.getEmployeeId() != null) {
            
            Employee employee = employeeRepository.findById(createUserDTO.getEmployeeId())
                .orElseThrow(EmployeeNotFoundException::new);

            createUserDTO.setEmployee(employee);
        
        }

        User newUser = createUserMapper.map(createUserDTO);
        
        newUser.setPasswordHash(passwordEncoderService.encodePassword(newUser.getUsername()));

        User savedUser = userRepository.save(newUser);

        return userResponseMapper.map(savedUser);

    }

    @Transactional
    public void delete(Long userId, String loggedUserRole) {

        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        if(user.getStatus() == UserStatus.INACTIVE) {
            throw new UserAlreadyInactiveException();
        }
        
        boolean isUserAdmin = user.getRole().equals(UserRole.ADMIN);
        boolean isLoggedUserAdmin = "ADMIN".equals(loggedUserRole);
        if(isUserAdmin && !isLoggedUserAdmin) {
            throw new AuthorizationDeniedException("You don't have permission to delete an admin user.");
        }

        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);

    }
    
}
