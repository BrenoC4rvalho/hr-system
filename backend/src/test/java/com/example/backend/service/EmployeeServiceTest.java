package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.example.backend.dto.CreateEmployeeDTO;
import com.example.backend.dto.EmployeeBasicDTO;
import com.example.backend.dto.EmployeeDTO;
import com.example.backend.exception.EmployeeNotFoundException;
import com.example.backend.exception.MonthNotValidException;
import com.example.backend.mapper.CreateEmployeeMapper;
import com.example.backend.mapper.EmployeeBasicMapper;
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

    @Mock 
    private EmployeeBasicMapper employeeBasicMapper;

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
        when(employeeRepository.findAll(any(Specification.class), eq(pageable)))
            .thenReturn(employeePage);
        when(employeeMapper.map(employee)).thenReturn(employeeDTO);

        Page<EmployeeDTO> result = employeeService.getAll(pageable, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getFirstName());

        verify(employeeRepository).findAll(any(Specification.class), eq(pageable));
        verify(employeeMapper).map(employee);
    }

    @Test
    void shouldThrowExceptionWhenNoEmployeesFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> emptyPage = Page.empty();

        when(employeeRepository.findAll(any(Specification.class), eq(pageable)))
            .thenReturn(emptyPage);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getAll(pageable, null, null, null));

        verify(employeeRepository).findAll(any(Specification.class), eq(pageable));
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

    @Test
    void shouldReturnEmployeesByFirstName() {
        Employee employee = new Employee();
        employee.setFirstName("Alice");

        EmployeeBasicDTO dto = new EmployeeBasicDTO();
        dto.setFirstName("Alice");

        when(employeeRepository.findAll(any(Specification.class))).thenReturn(List.of(employee));
        when(employeeBasicMapper.map(employee)).thenReturn(dto);

        List<EmployeeBasicDTO> result = employeeService.getEmployeesByFirstName("Alice", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getFirstName());

        verify(employeeRepository).findAll(any(Specification.class));
        verify(employeeBasicMapper).map(employee);
    }

    @Test
    void shouldThrowExceptionWhenFirstNameAndDepartmentIdAreNull() {
        assertThrows(IllegalArgumentException.class, () ->
            employeeService.getEmployeesByFirstName(null, null)
        );
    }

    @Test
    void shouldThrowEmployeeNotFoundExceptionWhenNoEmployeesMatch() {
        when(employeeRepository.findAll(any(Specification.class))).thenReturn(List.of());

        assertThrows(EmployeeNotFoundException.class, () ->
            employeeService.getEmployeesByFirstName("NoName", null)
        );

        verify(employeeRepository).findAll(any(Specification.class));
    }


    @Test
    void shouldReturnEmployeeStatusSummary() {
        Object[] row1 = new Object[]{"ACTIVE", 5L};
        Object[] row2 = new Object[]{"INACTIVE", 2L};

        when(employeeRepository.countEmployeesByStatus()).thenReturn(List.of(row1, row2));

        Map<String, Long> result = employeeService.getEmployeeStatusSummary();

        assertNotNull(result);
        assertEquals(0L, result.getOrDefault("ON_LEAVE", 0L)); // Supondo que existe esse status no enum
        assertEquals(5L, result.get("ACTIVE"));
        assertEquals(2L, result.get("INACTIVE"));

        verify(employeeRepository).countEmployeesByStatus();
    }

    @Test
    void shouldReturnEmployeesByBirthMonth() {
        Employee employee = new Employee();
        employee.setFirstName("Bob");

        EmployeeBasicDTO dto = new EmployeeBasicDTO();
        dto.setFirstName("Bob");

        when(employeeRepository.findByBirthMonth(5)).thenReturn(List.of(employee));
        when(employeeBasicMapper.map(employee)).thenReturn(dto);

        List<EmployeeBasicDTO> result = employeeService.getEmployeesByBirthMonth(5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bob", result.get(0).getFirstName());

        verify(employeeRepository).findByBirthMonth(5);
        verify(employeeBasicMapper).map(employee);
    }

    @Test
    void shouldThrowExceptionWhenBirthMonthIsInvalid() {
        assertThrows(MonthNotValidException.class, () ->
            employeeService.getEmployeesByBirthMonth(0)
        );

        assertThrows(MonthNotValidException.class, () ->
            employeeService.getEmployeesByBirthMonth(13)
        );
    }




}