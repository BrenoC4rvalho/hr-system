package com.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.example.backend.dto.PositionDTO;
import com.example.backend.exception.PositionNotFoundException;
import com.example.backend.mapper.PositionMapper;
import com.example.backend.model.Position;
import com.example.backend.repository.PositionRepository;

@Service
public class PositionService {


    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public PositionService(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    public List<PositionDTO> getAll() {
        List<Position> positions = positionRepository.findAll();

        if(positions.isEmpty()) {
            throw new PositionNotFoundException();
        }

        return positions.stream()
            .map(positionMapper::map)
            .collect(Collectors.toList());
    }

}
