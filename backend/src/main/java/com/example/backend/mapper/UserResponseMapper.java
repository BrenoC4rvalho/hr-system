package com.example.backend.mapper;

import org.springframework.stereotype.Component;

import com.example.backend.dto.UserRespondeDTO;
import com.example.backend.model.User;

@Component
public class UserResponseMapper {

    public UserRespondeDTO map(User user) {
        UserRespondeDTO userRespondeDTO = new UserRespondeDTO();
        userRespondeDTO.setId(user.getId());
        userRespondeDTO.setUsername(user.getUsername());
        userRespondeDTO.setRole(user.getRole());  
        userRespondeDTO.setEmployee(user.getEmployee());
        userRespondeDTO.setStatus(user.getStatus());
        
        return userRespondeDTO;
    }
    
}
