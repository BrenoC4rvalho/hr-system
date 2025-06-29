-- V8: create department embeddings table

CREATE TABLE department_embeddings (
    id UUID PRIMARY KEY,
    content TEXT,
    metadata JSONB,
    embedding VECTOR(768) NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
