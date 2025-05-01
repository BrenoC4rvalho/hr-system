package com.example.backend.mapper;

import org.springframework.stereotype.Component;

import com.example.backend.dto.EmployeeBirthdayDTO;
import com.example.backend.model.Employee;

@Component
public class EmployeeBirthdayMapper {

    public EmployeeBirthdayDTO map(Employee employee) {
        EmployeeBirthdayDTO employeeBirthdayDTO = new EmployeeBirthdayDTO();
        employeeBirthdayDTO.setId(employee.getId());
        employeeBirthdayDTO.setFirstName(employee.getFirstName());
        employeeBirthdayDTO.setLastName(employee.getLastName());
        employeeBirthdayDTO.setDepartment(employee.getDepartment());
        employeeBirthdayDTO.setBirthDate(employee.getBirthDate());
        return employeeBirthdayDTO;
    }
    
}
