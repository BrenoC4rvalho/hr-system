package com.example.backend.service;

import org.springframework.stereotype.Service;

import com.example.backend.repository.PositionRepository;

@Service
public class PositionService {

    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

}
