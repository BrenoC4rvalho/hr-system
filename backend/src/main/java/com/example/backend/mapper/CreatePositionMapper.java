package com.example.backend.mapper;

import org.springframework.stereotype.Component;

import com.example.backend.dto.CreatePositionDTO;
import com.example.backend.model.Position;

@Component
public class CreatePositionMapper {

    public Position map(CreatePositionDTO createPositionDTO) {
        Position position = new Position();
        position.setName(createPositionDTO.getName());
        position.setDescription(createPositionDTO.getDescription());
        return position;
    }
    
}
