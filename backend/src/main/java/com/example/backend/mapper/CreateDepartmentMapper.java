package com.example.backend.mapper;

import org.springframework.stereotype.Component;

import com.example.backend.dto.CreateDepartmentDTO;
import com.example.backend.model.Department;

@Component
public class CreateDepartmentMapper {

    public Department map(CreateDepartmentDTO createDepartmentDTO) {

        Department department = new Department();
        department.setName(createDepartmentDTO.getName());
        return department;

    }
    
}
