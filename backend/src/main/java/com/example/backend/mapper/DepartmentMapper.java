package com.example.backend.mapper;

import com.example.backend.dto.DepartmentDTO;
import com.example.backend.model.Department;

public class DepartmentMapper {

    public Department map(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setId(departmentDTO.getId());
        department.setName(departmentDTO.getName());
        department.setManagerId(departmentDTO.getManager());
        return department;
    }

    public DepartmentDTO map(Department department) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(department.getId());
        departmentDTO.setName(department.getName());
        departmentDTO.setManager(department.getManager());
        return departmentDTO;
    }
    
}
