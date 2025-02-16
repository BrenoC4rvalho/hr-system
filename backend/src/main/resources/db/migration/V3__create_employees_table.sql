-- V3: create Employees table

CREATE TYPE employee_gender_enum AS ENUM ('MALE', 'FEMALE', 'OTHER');
CREATE TYPE employee_shift_enum AS ENUM ('MORNING', 'AFTERNOON', 'NIGHT', 'NONE');
CREATE TYPE employee_status_enum AS ENUM ('ACTIVE', 'TERMINATED', 'ON_LEAVE', 'SICK_LEAVE', 'LEAVE_OF_ABSENCE', 'RESIGNED');

CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(30) NOT NULL CHECK,
    gender employee_gender_enum NOT NULL,
    department_id BIGINT NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id),
    position_id BIGINT NOT NULL,
    FOREIGN KEY (position_id) REFERENCES positions(id),
    shift employee_shift_enum NOT NULL DEFAULT 'NONE',
    status employee_status_enum NOT NULL DEFAULT 'ACTIVE',
    birth_date DATE NOT NULL,
    hired_date DATE,
    termination_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);