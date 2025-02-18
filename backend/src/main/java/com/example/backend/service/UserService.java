package com.example.backend.service;

import org.springframework.stereotype.Service;

import com.example.backend.dto.CreateUserDTO;
import com.example.backend.dto.UserRespondeDTO;
import com.example.backend.exception.EmployeeNotFoundException;
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

    public UserRespondeDTO create(CreateUserDTO createUserDTO) {
        
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

}
