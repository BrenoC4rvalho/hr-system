package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

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
    @DisplayName("create: Should create a new department")
    void create() {
        when(createDepartmentMapper.map(createDepartmentDTO)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(departmentMapper.map(department)).thenReturn(departmentDTO);

        DepartmentDTO result = departmentService.create(createDepartmentDTO);

        assertNotNull(result);
        verify(createDepartmentMapper).map(createDepartmentDTO);
        verify(departmentRepository).save(department);
        verify(departmentMapper).map(department);
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
    @DisplayName("getDepartment: Should return department when found")
    void getDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentMapper.map(department)).thenReturn(departmentDTO);

        DepartmentDTO result = departmentService.getDepartment(1L);

        assertNotNull(result);
        assertEquals("HR", result.getName());

        verify(departmentRepository).findById(1L);
        verify(departmentMapper).map(department);
    }

    @Test
    @DisplayName("getDepartment: Should throw exception when department is not found")
    void getDepartmentNotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getDepartment(1L));

        verify(departmentRepository).findById(1L);
        verifyNoInteractions(departmentMapper);
    }

    @Test
    @DisplayName("update: Should update department when department exists")
    void update() {
        // Mockando um departamento existente
        Department department = new Department();
        department.setId(1L);
        department.setName("Old Department");
    
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("Updated Department");
        when(departmentMapper.map(any(Department.class))).thenReturn(departmentDTO); 
    
        DepartmentDTO updatedDTO = new DepartmentDTO();
        updatedDTO.setName("Updated Department");
    
        DepartmentDTO result = departmentService.update(1L, updatedDTO);
    
        assertNotNull(result);
        assertEquals("Updated Department", result.getName());
    
        verify(departmentRepository).findById(1L);
        verify(departmentRepository).save(department);
        verify(departmentMapper).map(any(Department.class)); 
    }

    @Test
    @DisplayName("update: Should throw exception when department not found")
    void updateThrowsExceptionWhenNotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFoundException.class, () -> departmentService.update(1L, departmentDTO));
    }

    @Test
    @DisplayName("update: Should throw exception when name is too short")
    void updateThrowsExceptionForShortName() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        departmentDTO.setName("A");
        assertThrows(IllegalArgumentException.class, () -> departmentService.update(1L, departmentDTO));
    }

    @Test
    @DisplayName("update: Should throw exception when manager is from another department")
    void updateThrowsExceptionForInvalidManager() {
        manager = new Employee();
        manager.setId(2L);
        manager.setDepartment(new Department());
        
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(manager));
        
        departmentDTO.setManager(new Employee());
        departmentDTO.getManager().setId(2L);
        
        assertThrows(IllegalArgumentException.class, () -> departmentService.update(1L, departmentDTO));
    }
}
