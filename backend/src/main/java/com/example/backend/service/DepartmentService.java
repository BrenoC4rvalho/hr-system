package com.example.backend.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final VectorStore departmentVectorStore;
    private final DepartmentMapper departmentMapper;
    private final CreateDepartmentMapper createDepartmentMapper;

    public DepartmentService(
        DepartmentRepository departmentRepository,
        EmployeeRepository employeeRepository,
        @Qualifier("departmentVectorStore") VectorStore departmentVectorStore,
        DepartmentMapper departmentMapper, 
        CreateDepartmentMapper createDepartmentMapper
    ) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.departmentVectorStore = departmentVectorStore;
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

    @Transactional
    public DepartmentDTO create(CreateDepartmentDTO createDepartmentDTO) {
        Department department = createDepartmentMapper.map(createDepartmentDTO);
        department = departmentRepository.save(department);
        generateAndStoreEmbedding(department);
        return departmentMapper.map(department);
    }

    public DepartmentDTO getDepartment(Long id) {
        Department department = departmentRepository.findById(id)
           .orElseThrow(DepartmentNotFoundException::new);

        return departmentMapper.map(department);
    }

    @Transactional
    public DepartmentDTO update(Long id, DepartmentDTO departmentDTO) {

        Department department = departmentRepository.findById(id)
           .orElseThrow(DepartmentNotFoundException::new);

        if(departmentDTO.getName() != null && (departmentDTO.getName().length() < 2 || departmentDTO.getName().length() > 100)) {
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

        generateAndStoreEmbedding(updatedDepartment);

        return departmentMapper.map(updatedDepartment);

    }

    @Transactional
    public void generateAndStoreEmbedding(Department department) {
        String managerName = (department.getManager() != null)
                ? String.format("The manager is %s %s.", department.getManager().getFirstName(), department.getManager().getLastName())
                : "This department does not have a manager assigned.";

        String content = String.format(
                "Department: %s. %s",
                department.getName(),
                managerName
        );

        Document document = new Document(content, Map.of(
                "department_id", department.getId(),
                "department_name", department.getName()
        ));

        this.departmentVectorStore.add(List.of(document));
    }

}
