package com.example.backend.mapper;

import org.springframework.stereotype.Component;

import com.example.backend.dto.CreateUserDTO;
import com.example.backend.model.User;

@Component
public class CreateUserMapper {

    public User map(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setEmployee(createUserDTO.getEmployee());
        user.setRole(createUserDTO.getRole() != null ? createUserDTO.getRole() : user.getRole());  
        return user;
    }
    
}
