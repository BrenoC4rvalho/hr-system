-- V6: create employee embeddings table

CREATE TABLE employee_embeddings (
    id UUID PRIMARY KEY,
    content TEXT,
    metadata JSONB,
    embedding VECTOR(768) NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
