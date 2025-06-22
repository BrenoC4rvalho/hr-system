-- V7: create position embeddings table

CREATE TABLE position_embeddings (
    id UUID PRIMARY KEY,
    position_id BIGINT NOT NULL,
    context_chunk TEXT,
    embedding VECTOR(768) NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_position
        FOREIGN KEY(position_id)
        REFERENCES positions(id)
        ON DELETE CASCADE
);
