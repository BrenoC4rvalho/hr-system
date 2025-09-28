package com.example.backend.controller;

import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.CreateEmployeeDTO;
import com.example.backend.dto.EmployeeBasicDTO;
import com.example.backend.dto.EmployeeDTO;
import com.example.backend.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping() 
    public ResponseEntity<?> index(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "12") int size,
        @RequestParam(required = false) Long positionId,
        @RequestParam(required = false) Long departmentId,
        @RequestParam(required = false) String name
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeDTO> employees = employeeService.getAll(pageable, positionId, departmentId, name);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("totalEmployees", employees.getTotalElements());
        response.put("currentPage", employees.getNumber());
        response.put("totalPages", employees.getTotalPages());
        response.put("pageSize", employees.getSize());
        response.put("employees", employees.getContent());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody CreateEmployeeDTO createEmployeeDTO) {
        EmployeeDTO newEmployee = employeeService.create(createEmployeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.getEmployee(id);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
        @PathVariable Long id,
        @RequestBody EmployeeDTO employeeDTO
    ) {
        EmployeeDTO updatedEmployee = employeeService.update(id, employeeDTO);
        return  ResponseEntity.status(HttpStatus.OK).body(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        employeeService.delete(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "The employee has been deleted.");

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/birthdays")
    public ResponseEntity<?> getEmployeesByBirthMonth(@RequestParam int month) {
        List<EmployeeBasicDTO> employees = employeeService.getEmployeesByBirthMonth(month);
        
        String monthName = Month.of(month).getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("month", monthName);
        response.put("monthNumber", month);
        response.put("totalEmployees", employees.size());
        response.put("employees", employees);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping("/status-summary")
    public ResponseEntity<?> getEmployeeStatusSummary() {
        Map<String, Long> summary = employeeService.getEmployeeStatusSummary();
        return ResponseEntity.status(HttpStatus.OK).body(summary);
    }


    @GetMapping("/shift-summary")
    public ResponseEntity<?> getEmployeeShiftSummary() {
        Map<String, Long> summary = employeeService.getEmployeeShiftSummary();
        return ResponseEntity.status(HttpStatus.OK).body(summary);
    }

    @GetMapping("/recent-hires")
    public ResponseEntity<?> getRecentHires(@RequestParam(defaultValue = "90") int days) {
        List<EmployeeBasicDTO> employees = employeeService.getRecentHires(days);
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getEmployeeByName(
        @RequestParam String firstName,
        @RequestParam(required = false) Long departmentId 
    ) {
        List<EmployeeBasicDTO> employees = employeeService.getEmployeesByFirstName(firstName, departmentId);
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }
    
    
}
