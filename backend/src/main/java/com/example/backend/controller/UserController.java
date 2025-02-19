package com.example.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.CreateUserDTO;
import com.example.backend.dto.UserRespondeDTO;
import com.example.backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
 
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @PostMapping()
    public ResponseEntity<?> create(HttpServletRequest request, @Valid @RequestBody CreateUserDTO createUserDTO) {
        String roleOfCreatorUser = request.getAttribute("user_role").toString();
        UserRespondeDTO newUser = userService.create(createUserDTO, roleOfCreatorUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

}
