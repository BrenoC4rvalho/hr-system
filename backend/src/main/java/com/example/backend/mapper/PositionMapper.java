package com.example.backend.mapper;

import com.example.backend.dto.PositionDTO;
import com.example.backend.model.Position;

public class PositionMapper {

    public Position map(PositionDTO positionDTO) {
        Position position = new Position();
        position.setId(positionDTO.getId());
        position.setName(positionDTO.getName());
        position.setDescription(positionDTO.getDescription());
        return position;
    }

    public PositionDTO map(Position position) {
        PositionDTO positionDTO = new PositionDTO();
        positionDTO.setId(position.getId());
        positionDTO.setName(position.getName());
        positionDTO.setDescription(position.getDescription());
        return positionDTO;
    }
    
}
