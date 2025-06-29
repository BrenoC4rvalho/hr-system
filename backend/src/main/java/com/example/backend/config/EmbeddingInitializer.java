package com.example.backend.config;

import com.example.backend.model.Department;
import com.example.backend.model.Employee;
import com.example.backend.model.Position;
import com.example.backend.repository.DepartmentRepository;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.PositionRepository;
import com.example.backend.service.DepartmentService;
import com.example.backend.service.EmployeeService;
import com.example.backend.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class EmbeddingInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddingInitializer.class);

    private final JdbcTemplate jdbcTemplate;
    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final PositionService positionService;
    private final PositionRepository positionRepository;

    public EmbeddingInitializer(
            JdbcTemplate jdbcTemplate,
            DepartmentService departmentService,
            DepartmentRepository departmentRepository,
            EmployeeService employeeService,
            EmployeeRepository employeeRepository,
            PositionService positionService,
            PositionRepository positionRepository
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.departmentService = departmentService;
        this.departmentRepository = departmentRepository;
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.positionService = positionService;
        this.positionRepository = positionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeEmbeddingsFor("department_embeddings", "departments", () -> {
            List<Department> allDepartments = departmentRepository.findAll();
            logger.info("Generating embeddings for {} departments.", allDepartments.size());
            allDepartments.forEach(departmentService::generateAndStoreEmbedding);
        });

        initializeEmbeddingsFor("employee_embeddings", "employees", () -> {
            List<Employee> allEmployees = employeeRepository.findAll();
            logger.info("Generating embeddings for {} employees.", allEmployees.size());
            allEmployees.forEach(employeeService::generateAndStoreEmbedding);
        });

        initializeEmbeddingsFor("position_embeddings", "positions", () -> {
            List<Position> allPositions = positionRepository.findAll();
            logger.info("Generating embeddings for {} positions.", allPositions.size());
            allPositions.forEach(positionService::generateAndStoreEmbedding);
        });
    }

    private void initializeEmbeddingsFor(String embeddingTableName, String sourceTableName, Runnable embeddingAction) {
        String sql = "SELECT COUNT(*) FROM " + embeddingTableName;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

        if (count != null && count == 0) {
            logger.info("Embedding table '{}' is empty. Populating with data from '{}'.", embeddingTableName, sourceTableName);
            embeddingAction.run();
            logger.info("Finished populating embeddings for '{}'.", embeddingTableName);
        } else {
            logger.info("Embedding table '{}' already contains data. Skipping initialization.", embeddingTableName);
        }
    }


}
