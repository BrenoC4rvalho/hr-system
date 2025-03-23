package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.backend.dto.CreateEmployeeDTO;
import com.example.backend.dto.EmployeeDTO;
import com.example.backend.exception.EmployeeNotFoundException;
import com.example.backend.mapper.CreateEmployeeMapper;
import com.example.backend.mapper.EmployeeMapper;
import com.example.backend.model.Employee;
import com.example.backend.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CreateEmployeeMapper createEmployeeMapper;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void shouldReturnAllEmployees() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> employeePage = new PageImpl<>(List.of(employee));
        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);
        when(employeeMapper.map(employee)).thenReturn(employeeDTO);

        Page<EmployeeDTO> result = employeeService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getFirstName());

        verify(employeeRepository).findAll(pageable);
        verify(employeeMapper).map(employee);
    }

    @Test
    void shouldThrowExceptionWhenNoEmployeesFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> emptyPage = Page.empty();

        when(employeeRepository.findAll(pageable)).thenReturn(emptyPage);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getAll(pageable));

        verify(employeeRepository).findAll(pageable);
    }

    @Test
    void shouldCreateEmployee() {

        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO();
        createEmployeeDTO.setFirstName("John");
        createEmployeeDTO.setLastName("Doe");
        createEmployeeDTO.setEmail("john.doe@example.com");

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");

        when(createEmployeeMapper.map(createEmployeeDTO)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.map(employee)).thenReturn(employeeDTO);

        EmployeeDTO result = employeeService.create(createEmployeeDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());

        verify(createEmployeeMapper).map(createEmployeeDTO);
        verify(employeeRepository).save(employee);
        verify(employeeMapper).map(employee);
    }

    @Test
    void shouldReturnEmployeeById() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setFirstName("John");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.map(employee)).thenReturn(employeeDTO);

        EmployeeDTO result = employeeService.getEmployee(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());

        verify(employeeRepository).findById(1L);
        verify(employeeMapper).map(employee);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployee(1L));

        verify(employeeRepository).findById(1L);
    }

    @Test
    void shouldUpdateEmployee() {
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setFirstName("John");

        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
        updatedEmployeeDTO.setFirstName("Updated John");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1L);
        updatedEmployee.setFirstName("Updated John");

        EmployeeDTO expectedDTO = new EmployeeDTO();
        expectedDTO.setId(1L);
        expectedDTO.setFirstName("Updated John");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);
        when(employeeMapper.map(updatedEmployee)).thenReturn(expectedDTO);

        EmployeeDTO result = employeeService.update(1L, updatedEmployeeDTO);

        assertNotNull(result);
        assertEquals("Updated John", result.getFirstName());

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any(Employee.class));
        verify(employeeMapper).map(updatedEmployee);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("Updated John");

        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.update(1L, employeeDTO));

        verify(employeeRepository).findById(1L);
    }
}