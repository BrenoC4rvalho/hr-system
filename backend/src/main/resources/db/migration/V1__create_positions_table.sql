-- V1: Create Positions Table (Initial schema)

CREATE TABLE positions (
    id BIGSERIAL PRIMARY KEY, 
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
