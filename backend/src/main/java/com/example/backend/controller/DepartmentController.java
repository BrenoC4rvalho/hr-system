package com.example.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.DepartmentDTO;
import com.example.backend.service.DepartmentService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/departments")
public class DepartmentController {
    
    public final DepartmentService departmentService;
    

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping()
    public ResponseEntity<?> index() {
        List<DepartmentDTO> departments = departmentService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(departments);
    }
    
}
