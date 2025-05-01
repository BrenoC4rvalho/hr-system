package com.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.backend.dto.CreateEmployeeDTO;
import com.example.backend.dto.EmployeeBirthdayDTO;
import com.example.backend.dto.EmployeeDTO;
import com.example.backend.exception.EmployeeNotFoundException;
import com.example.backend.exception.MonthNotValidException;
import com.example.backend.mapper.CreateEmployeeMapper;
import com.example.backend.mapper.EmployeeBirthdayMapper;
import com.example.backend.mapper.EmployeeMapper;
import com.example.backend.model.Employee;
import com.example.backend.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CreateEmployeeMapper createEmployeeMapper;
    private final EmployeeMapper employeeMapper;
    private final EmployeeBirthdayMapper employeeBirthdayMapper;

    public EmployeeService(
        EmployeeRepository employeeRepository, 
        CreateEmployeeMapper createEmployeeMapper, 
        EmployeeMapper employeeMapper,
        EmployeeBirthdayMapper employeeBirthdayMapper     
    ) {
        this.employeeRepository = employeeRepository;
        this.createEmployeeMapper = createEmployeeMapper;
        this.employeeMapper = employeeMapper;
        this.employeeBirthdayMapper = employeeBirthdayMapper;
    }

    public Page<EmployeeDTO> getAll(Pageable pageable, Long positionId, Long departmentId, String name) {

        Specification<Employee> spec = Specification.where(null);

        if (positionId != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.join("position").get("id"), positionId)
            );
        }

        if (departmentId != null) {
            spec = spec.and((root, query, cb) ->
                cb.equal(root.join("department").get("id"), departmentId)
            );
        }

        if (name != null && !name.isBlank()) {
            spec = spec.and((root, query, cb) ->
                cb.like(cb.lower(root.get("firstName")), "%" + name.toLowerCase() + "%")
            );
        }

        Page<Employee> employees = employeeRepository.findAll(spec, pageable);

        if (employees.isEmpty()) {
            throw new EmployeeNotFoundException();
        }

        return employees.map(employeeMapper::map);

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

    @Transactional
    public EmployeeDTO update(Long id, EmployeeDTO employeeDTO) {

        Employee employee = employeeRepository.findById(id)
           .orElseThrow(EmployeeNotFoundException::new);

        if (employeeDTO.getFirstName() != null) employee.setFirstName(employeeDTO.getFirstName());
        if (employeeDTO.getLastName() != null) employee.setLastName(employeeDTO.getLastName());
        if (employeeDTO.getEmail() != null) employee.setEmail(employeeDTO.getEmail());
        if (employeeDTO.getPhone() != null) employee.setPhone(employeeDTO.getPhone());
        if (employeeDTO.getGender() != null) employee.setGender(employeeDTO.getGender());
        if (employeeDTO.getDepartment() != null) employee.setDepartment(employeeDTO.getDepartment());
        if (employeeDTO.getPosition() != null) employee.setPosition(employeeDTO.getPosition());
        if (employeeDTO.getShift() != null) employee.setShift(employeeDTO.getShift());
        if (employeeDTO.getStatus() != null) employee.setStatus(employeeDTO.getStatus());
        if (employeeDTO.getBirthDate() != null) employee.setBirthDate(employeeDTO.getBirthDate());
        if (employeeDTO.getHiredDate() != null) employee.setHiredDate(employeeDTO.getHiredDate());
        if (employeeDTO.getTerminationDate() != null) employee.setTerminationDate(employeeDTO.getTerminationDate());

        Employee employeeUpdated = employeeRepository.save(employee);
        return employeeMapper.map(employeeUpdated);
    }

    public void delete(Long id) {
    
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(EmployeeNotFoundException::new);
            
        // update termination date 
        // update status

    }

    public List<EmployeeBirthdayDTO> getEmployeesByBirthMonth(int month) {

        if(month < 1 || month > 12) {
            throw new MonthNotValidException();
        }

        List<Employee> employees = employeeRepository.findByBirthMonth(month);

        if (employees.isEmpty()) {
            throw new EmployeeNotFoundException();
        }

        return employees.stream()
            .map(employeeBirthdayMapper::map)
            .collect(Collectors.toList());
        
    }

}
