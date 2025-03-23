package com.example.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.dto.CreateEmployeeDTO;
import com.example.backend.dto.EmployeeDTO;
import com.example.backend.mapper.CreateEmployeeMapper;
import com.example.backend.mapper.EmployeeMapper;
import com.example.backend.model.Employee;
import com.example.backend.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CreateEmployeeMapper createEmployeeMapper;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;


    private Employee employee;
    private EmployeeDTO employeeDTO;
    private CreateEmployeeDTO createEmployeeDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        
        employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        
        createEmployeeDTO = new CreateEmployeeDTO();
        createEmployeeDTO.setFirstName("John");
        createEmployeeDTO.setLastName("Doe");
    }

}
