package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @DisplayName("Test get all employees.")
    @Test
    void shouldReturnAllEmployees() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Employee> employeeList = List.of(employee);
        Page<Employee> employeePage = new PageImpl<>(employeeList, pageable, employeeList.size());
    
        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);
    
        Page<EmployeeDTO> result = employeeService.getAll(pageable);
    
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }
}
