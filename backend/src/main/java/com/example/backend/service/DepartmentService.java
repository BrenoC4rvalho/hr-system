package com.example.backend.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.dto.CreateDepartmentDTO;
import com.example.backend.dto.DepartmentDTO;
import com.example.backend.exception.DepartmentNotFoundException;
import com.example.backend.exception.EmployeeNotFoundException;
import com.example.backend.mapper.CreateDepartmentMapper;
import com.example.backend.mapper.DepartmentMapper;
import com.example.backend.model.Department;
import com.example.backend.model.Employee;
import com.example.backend.repository.DepartmentRepository;
import com.example.backend.repository.EmployeeRepository;

@Service
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;  
    private final DepartmentMapper departmentMapper;
    private final CreateDepartmentMapper createDepartmentMapper;

    public DepartmentService(
        DepartmentRepository departmentRepository,
        EmployeeRepository employeeRepository, 
        DepartmentMapper departmentMapper, 
        CreateDepartmentMapper createDepartmentMapper
    ) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
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

    public DepartmentDTO update(Long id, DepartmentDTO departmentDTO) {

        Department department = departmentRepository.findById(id)
           .orElseThrow(() -> new DepartmentNotFoundException());

        if(departmentDTO.getName().length() < 2 || departmentDTO.getName().length() > 100) {
            throw new IllegalArgumentException("Name must be between 2 and 100 characters long.");
        }

        if(departmentDTO.getManager() != null) {
            Employee manager = employeeRepository.findById(departmentDTO.getManager().getId())
                .orElseThrow(EmployeeNotFoundException::new);
            
            if(!manager.getDepartment().equals(department)) {
                throw new IllegalArgumentException("Manager must be in the same department.");
            }

            department.setManager(manager);
        }

        if(departmentDTO.getName() != null) {
            department.setName(departmentDTO.getName());
        }

        Department updatedDepartment = departmentRepository.save(department);

        return departmentMapper.map(updatedDepartment);

    }

}
