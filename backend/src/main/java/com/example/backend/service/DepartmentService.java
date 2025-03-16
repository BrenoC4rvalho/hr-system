package com.example.backend.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.dto.CreateDepartmentDTO;
import com.example.backend.dto.DepartmentDTO;
import com.example.backend.exception.DepartmentNotFoundException;
import com.example.backend.mapper.CreateDepartmentMapper;
import com.example.backend.mapper.DepartmentMapper;
import com.example.backend.model.Department;
import com.example.backend.repository.DepartmentRepository;

@Service
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final CreateDepartmentMapper createDepartmentMapper;

    public DepartmentService(
        DepartmentRepository departmentRepository, 
        DepartmentMapper departmentMapper, 
        CreateDepartmentMapper createDepartmentMapper
    ) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.createDepartmentMapper = createDepartmentMapper;
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

    public DepartmentDTO create(CreateDepartmentDTO createDepartmentDTO) {
        Department department = createDepartmentMapper.map(createDepartmentDTO);
        department = departmentRepository.save(department);
        return departmentMapper.map(department);
    }

    public DepartmentDTO getDepartment(Long id) {
        Department department = departmentRepository.findById(id)
           .orElseThrow(() -> new DepartmentNotFoundException());

        return departmentMapper.map(department);
    }

}
