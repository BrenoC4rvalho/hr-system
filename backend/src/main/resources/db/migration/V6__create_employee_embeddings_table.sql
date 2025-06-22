-- V6: create employee embeddings table

CREATE TABLE employee_embeddings (
    id UUID PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    context_chunk TEXT,
    embedding VECTOR(768) NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_employee
        FOREIGN KEY(employee_id)
        REFERENCES employees(id)
        ON DELETE CASCADE
);
