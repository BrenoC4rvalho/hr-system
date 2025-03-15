package com.example.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    
    public final DepartmentService departmentService;
    

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

}
