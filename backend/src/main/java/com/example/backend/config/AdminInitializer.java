package com.example.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.backend.enums.UserRole;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.service.PasswordEncoderService;

@Component
public class AdminInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    public AdminInitializer(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    @Override
    public void run(String... args) throws Exception {
        createInitialAdmin();
    }

    private void createInitialAdmin() {

        if(!userRepository.existsByRole(UserRole.ADMIN)) {
            
            User admin = new User();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoderService.encodePassword("password"));
            admin.setRole(UserRole.ADMIN);

            userRepository.save(admin);
            System.out.println("The admin has been created successfully.");

        }

    }


}
