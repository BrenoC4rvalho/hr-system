package com.example.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.CreateEmployeeDTO;
import com.example.backend.dto.EmployeeDTO;
import com.example.backend.exception.EmployeeNotFoundException;
import com.example.backend.mapper.CreateEmployeeMapper;
import com.example.backend.mapper.EmployeeMapper;
import com.example.backend.model.Employee;
import com.example.backend.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CreateEmployeeMapper createEmployeeMapper;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, CreateEmployeeMapper createEmployeeMapper, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.createEmployeeMapper = createEmployeeMapper;
        this.employeeMapper = employeeMapper;
    }

    @Transactional
    public EmployeeDTO create(CreateEmployeeDTO createEmployeeDTO) {
        Employee newEmployee = createEmployeeMapper.map(createEmployeeDTO);
        Employee savedEmployee = employeeRepository.save(newEmployee);
        return employeeMapper.map(savedEmployee);
    }

    public EmployeeDTO getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
               .orElseThrow(EmployeeNotFoundException::new);
        return employeeMapper.map(employee);
    }

    public void delete(Long id) {
    
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(EmployeeNotFoundException::new);
            
        // update termination date 
        // update status

    }
}
