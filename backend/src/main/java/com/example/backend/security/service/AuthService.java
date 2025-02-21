package com.example.backend.security.service;

import org.springframework.stereotype.Service;

import com.example.backend.dto.AuthRequestDTO;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.enums.UserStatus;
import com.example.backend.exception.AuthFailedException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final TokenService tokenService;

    public AuthService(
        UserRepository userRepository, 
        PasswordEncoderService passwordEncoderService, 
        TokenService tokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.tokenService = tokenService;
    }

    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) {
        
        User user = userRepository.findByUsername(authRequestDTO.getUsername())
            .orElseThrow(AuthFailedException::new);

        if(user.getStatus().equals(UserStatus.INACTIVE)) {
            throw new AuthFailedException();
        }
        
        Boolean authenticatedPassword = passwordEncoderService.matchPassword(authRequestDTO.getPassword(), user.getPasswordHash());
        if(!authenticatedPassword) {
            throw new AuthFailedException();
        }

        String token = tokenService.generateToken(user.getId(), user.getRole());
        return new AuthResponseDTO(token);
    }

}
