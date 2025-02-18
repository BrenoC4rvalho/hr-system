package com.example.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.CreateUserDTO;
import com.example.backend.dto.UserRespondeDTO;
import com.example.backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
 
    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody CreateUserDTO createUserDTO) {
        
        UserRespondeDTO newUser = userService.create(createUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

    }

}
