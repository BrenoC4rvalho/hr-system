package com.example.backend.mapper;

import org.springframework.stereotype.Component;

import com.example.backend.dto.EmployeeDTO;
import com.example.backend.model.Employee;

@Component
public class EmployeeMapper {

    public Employee map(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhone(employeeDTO.getPhone());
        employee.setGender(employeeDTO.getGender());
        employee.setDepartment(employeeDTO.getDepartment());
        employee.setPosition(employeeDTO.getPosition());
        employee.setShift(employeeDTO.getShift());
        employee.setStatus(employeeDTO.getStatus());
        employee.setBirthDate(employeeDTO.getBirthDate());
        employee.setHiredDate(employeeDTO.getHiredDate());
        employee.setTerminationDate(employeeDTO.getTerminationDate());
        return employee;
    }

    public EmployeeDTO map(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setPhone(employee.getPhone());
        employeeDTO.setGender(employee.getGender());
        employeeDTO.setDepartment(employee.getDepartment());
        employeeDTO.setPosition(employee.getPosition());
        employeeDTO.setShift(employee.getShift());
        employeeDTO.setStatus(employee.getStatus());
        employeeDTO.setBirthDate(employee.getBirthDate());
        employeeDTO.setHiredDate(employee.getHiredDate());
        employeeDTO.setTerminationDate(employee.getTerminationDate());
        return employeeDTO;
    }
    
}
