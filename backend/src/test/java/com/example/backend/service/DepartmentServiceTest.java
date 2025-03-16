package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.dto.CreateDepartmentDTO;
import com.example.backend.dto.DepartmentDTO;
import com.example.backend.exception.DepartmentNotFoundException;
import com.example.backend.mapper.CreateDepartmentMapper;
import com.example.backend.mapper.DepartmentMapper;
import com.example.backend.model.Department;
import com.example.backend.model.Employee;
import com.example.backend.repository.DepartmentRepository;
import com.example.backend.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {
    
     @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private CreateDepartmentMapper createDepartmentMapper;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;
    private DepartmentDTO departmentDTO;
    private CreateDepartmentDTO createDepartmentDTO;
    private Employee manager;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("HR");

        departmentDTO = new DepartmentDTO();
        departmentDTO.setName("HR");

        createDepartmentDTO = new CreateDepartmentDTO();
        createDepartmentDTO.setName("HR");
    }

    
    @Test
    void testCreate() {

    }

   @Test
    @DisplayName("getAll: Should return all departments")
    void getAll() {
        when(departmentRepository.findAll()).thenReturn(List.of(department));
        when(departmentMapper.map(department)).thenReturn(departmentDTO);

        List<DepartmentDTO> result = departmentService.getAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(departmentRepository).findAll();
        verify(departmentMapper).map(department);
    }

    @Test
    @DisplayName("getAll: Should throw exception when no departments found")
    void getAllThrowsException() {
        when(departmentRepository.findAll()).thenReturn(List.of());
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getAll());
    }
    
    @Test
    void testGetDepartment() {

    }

    @Test
    void testUpdate() {

    }
}
