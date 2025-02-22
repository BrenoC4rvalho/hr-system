package com.example.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.CreateUserDTO;
import com.example.backend.dto.UserRespondeDTO;
import com.example.backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @GetMapping()
    public ResponseEntity<?> index(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserRespondeDTO> users = userService.getAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
    
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @PostMapping()
    public ResponseEntity<?> create(HttpServletRequest request, @Valid @RequestBody CreateUserDTO createUserDTO) {
        String roleOfCreatorUser = request.getAttribute("user_role").toString();
        UserRespondeDTO newUser = userService.create(createUserDTO, roleOfCreatorUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER') or #id.toString() == authentication.name")
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable("id") Long id) {
        UserRespondeDTO user = userService.getUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    

    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?>  delete(HttpServletRequest request, @PathVariable("id") Long id) {
        String loggedUserRole = request.getAttribute("user_role").toString();
        userService.delete(id, loggedUserRole);
        return ResponseEntity.status(HttpStatus.OK).body("The user has been deleted.");
    }

}
