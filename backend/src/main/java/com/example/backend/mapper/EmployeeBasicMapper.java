package com.example.backend.mapper;

import org.springframework.stereotype.Component;

import com.example.backend.dto.EmployeeBasicDTO;
import com.example.backend.model.Employee;

@Component
public class EmployeeBasicMapper {

    public EmployeeBasicDTO map(Employee employee) {
        EmployeeBasicDTO employeeBasicTO = new EmployeeBasicDTO();
        employeeBasicTO.setId(employee.getId());
        employeeBasicTO.setFirstName(employee.getFirstName());
        employeeBasicTO.setLastName(employee.getLastName());
        employeeBasicTO.setDepartment(employee.getDepartment());
        employeeBasicTO.setBirthDate(employee.getBirthDate());
        return employeeBasicTO;
    }
    
}
