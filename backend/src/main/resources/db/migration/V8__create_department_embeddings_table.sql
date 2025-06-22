-- V8: create department embeddings table

CREATE TABLE department_embeddings (
    id UUID PRIMARY KEY,
    department_id BIGINT NOT NULL,
    context_chunk TEXT,
    embedding VECTOR(768) NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_department
        FOREIGN KEY(department_id)
        REFERENCES departments(id)
        ON DELETE CASCADE
);
