package com.example.backend.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.dto.DepartmentDTO;
import com.example.backend.exception.DepartmentNotFoundException;
import com.example.backend.mapper.DepartmentMapper;
import com.example.backend.model.Department;
import com.example.backend.repository.DepartmentRepository;

@Service
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public List<DepartmentDTO> getAll() {
        
        List<Department> departments = departmentRepository.findAll();

        if(departments.isEmpty()) {
            throw new DepartmentNotFoundException();
        }

        return departments.stream()
            .map(departmentMapper::map)
            .collect(Collectors.toList());
    }

    public DepartmentDTO getDepartment(Long id) {
        Department department = departmentRepository.findById(id)
           .orElseThrow(() -> new DepartmentNotFoundException());

        return departmentMapper.map(department);
    }

}
