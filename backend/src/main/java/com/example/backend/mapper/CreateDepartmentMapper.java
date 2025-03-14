package com.example.backend.mapper;

import com.example.backend.dto.DepartmentDTO;
import com.example.backend.model.Department;

public class CreateDepartmentMapper {

    public Department map(DepartmentDTO departmentDTO) {

        Department department = new Department();
        department.setName(departmentDTO.getName());
        return department;

    }
    
}
