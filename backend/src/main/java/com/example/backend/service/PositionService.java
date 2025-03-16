package com.example.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.example.backend.dto.CreatePositionDTO;
import com.example.backend.dto.PositionDTO;
import com.example.backend.exception.PositionNotFoundException;
import com.example.backend.mapper.CreatePositionMapper;
import com.example.backend.mapper.PositionMapper;
import com.example.backend.model.Position;
import com.example.backend.repository.PositionRepository;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;
    private final CreatePositionMapper createPositionMapper;

    public PositionService(PositionRepository positionRepository, PositionMapper positionMapper, CreatePositionMapper createPositionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
        this.createPositionMapper = createPositionMapper;
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

    public PositionDTO create(CreatePositionDTO createPositionDTO) {

        Position newPosition = createPositionMapper.map(createPositionDTO);

        newPosition = positionRepository.save(newPosition);

        return positionMapper.map(newPosition);
    }

    public PositionDTO getPosition(Long id)  {
        Position position = positionRepository.findById(id)
           .orElseThrow(() -> new PositionNotFoundException());

        return positionMapper.map(position);
    }

    public PositionDTO update(Long id, PositionDTO positionDTO) {
        Position position = positionRepository.findById(id)
           .orElseThrow(() -> new PositionNotFoundException());

        if(positionDTO.getName().length() < 2 || positionDTO.getName().length() > 100) {
            throw new IllegalArgumentException("Name must be between 2 and 100 characters long.");
        }

        if(positionDTO.getDescription().length() > 255) {
            throw new IllegalArgumentException("Description cannot be more than 255 characters long.");
        }

        if(positionDTO.getName() != null) {
            position.setName(positionDTO.getName().trim());
        }

        if(positionDTO.getDescription()!= null) {
            position.setDescription(positionDTO.getDescription().trim());
        }

        positionRepository.save(position);

        return positionMapper.map(position);
    }

}
