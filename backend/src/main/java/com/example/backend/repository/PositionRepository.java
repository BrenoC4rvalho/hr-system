package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Position;


public interface PositionRepository extends JpaRepository<Position, Long> {
    
}
