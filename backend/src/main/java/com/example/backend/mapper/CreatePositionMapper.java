package com.example.backend.mapper;

import com.example.backend.dto.CreatePositionDTO;
import com.example.backend.model.Position;

public class CreatePositionMapper {

    public Position map(CreatePositionDTO createPositionDTO) {
        Position position = new Position();
        position.setName(createPositionDTO.getName());
        position.setDescription(createPositionDTO.getDescription());
        return position;
    }
    
}
