package com.example.backend.mapper;

import com.example.backend.dto.CreateEmployeeDTO;
import com.example.backend.model.Employee;

public class CreateEmployeeMapper {

    public Employee map(CreateEmployeeDTO createEmployeeDTO) {
        Employee employee = new Employee();
        employee.setFirstName(createEmployeeDTO.getFirstName());
        employee.setLastName(createEmployeeDTO.getLastName());
        employee.setEmail(createEmployeeDTO.getEmail());
        employee.setPhone(createEmployeeDTO.getPhone());
        employee.setGender(createEmployeeDTO.getGender());
        employee.setDepartment(createEmployeeDTO.getDepartment());
        employee.setPosition(createEmployeeDTO.getPosition());
        employee.setShift(createEmployeeDTO.getShift());
        employee.setStatus(createEmployeeDTO.getStatus());
        employee.setBirthDate(createEmployeeDTO.getBirthDate());
        employee.setHiredDate(createEmployeeDTO.getHiredDate());
        return employee;
    }
    
}
