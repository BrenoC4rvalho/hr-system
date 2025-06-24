package com.example.backend.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class VectorStoreConfig {

    private static final int EMBEDDING_DIMENSION = 768;

    @Bean
    @Qualifier("employeeVectorStore")
    public VectorStore employeeVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName("employee_embeddings")
                .dimensions(EMBEDDING_DIMENSION)
                .initializeSchema(false) // flyway has already created the table
                .build();
    }

    @Bean
    @Qualifier("positionVectorStore")
    public VectorStore positionVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName("position_embeddings")
                .dimensions(EMBEDDING_DIMENSION)
                .initializeSchema(false) // flyway has already created the table
                .build();
    }

    @Bean
    @Qualifier("departmentVectorStore")
    public VectorStore departmentVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName("department_embeddings")
                .dimensions(EMBEDDING_DIMENSION)
                .initializeSchema(false) // flyway has already created the table
                .build();
    }

}
