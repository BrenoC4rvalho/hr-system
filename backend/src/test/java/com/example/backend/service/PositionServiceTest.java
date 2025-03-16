package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.backend.dto.PositionDTO;
import com.example.backend.exception.PositionNotFoundException;
import com.example.backend.mapper.CreatePositionMapper;
import com.example.backend.dto.CreatePositionDTO;
import com.example.backend.mapper.PositionMapper;
import com.example.backend.model.Position;
import com.example.backend.repository.PositionRepository;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private PositionMapper positionMapper;

    @Mock
    private CreatePositionMapper createPositionMapper;

    @InjectMocks
    private PositionService positionService;

    private Position position;
    private PositionDTO positionDTO;
    private CreatePositionDTO createPositionDTO;

    @BeforeEach
    void setUp() {
        position = new Position();
        position.setId(1L);
        position.setName("Manager");
        position.setDescription("Manages employees");

        positionDTO = new PositionDTO();

        createPositionDTO = new CreatePositionDTO();
        createPositionDTO.setName("Manager");
        createPositionDTO.setDescription("Manages employees");
    }

    @DisplayName("getPosition: Should return position when position exists.")
    @Test
    void getPosition() {

        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(positionMapper.map(position)).thenReturn(positionDTO);

        PositionDTO result = positionService.getPosition(1L);

        assertNotNull(result);
        verify(positionRepository).findById(1L);
        verify(positionMapper).map(position);
    }

    @DisplayName("getPosition: Should throw exception when position not found.")
    @Test
    void getPositionNotFound() {

        when(positionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PositionNotFoundException.class, () -> positionService.getPosition(1L));

    }

    @Test
    @DisplayName("create: Should create a new position")
    void testCreate() {
        when(createPositionMapper.map(createPositionDTO)).thenReturn(position);
        when(positionRepository.save(position)).thenReturn(position);
        when(positionMapper.map(position)).thenReturn(positionDTO);

        PositionDTO result = positionService.create(createPositionDTO);

        assertNotNull(result);
        verify(createPositionMapper).map(createPositionDTO);
        verify(positionRepository).save(position);
        verify(positionMapper).map(position);
    }

    @Test
    @DisplayName("create: Should throw exception when name is too short")
    void testCreateThrowsExceptionForShortName() {
        createPositionDTO.setName("A");
        assertThrows(IllegalArgumentException.class, () -> positionService.create(createPositionDTO));
    }

    @Test
    @DisplayName("create: Should throw exception when description is too long")
    void testCreateThrowsExceptionForLongDescription() {
        createPositionDTO.setDescription("A".repeat(256));
        assertThrows(IllegalArgumentException.class, () -> positionService.create(createPositionDTO));
    }

    @Test
    @DisplayName("getAllPosition: should return all positions")
    void GetAll() {

        when(positionRepository.findAll()).thenReturn(List.of(position));
        when(positionMapper.map(position)).thenReturn(positionDTO);
 
        List<PositionDTO> result = positionService.getAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());

        verify(positionRepository).findAll();
        verify(positionMapper).map(position);
    }

    @Test
    @DisplayName("getAllPositions: should throw exception when positions not found")
    void getAllPositionsNotFound() {
        when(positionRepository.findAll()).thenReturn(List.of());
        assertThrows(PositionNotFoundException.class, () -> positionService.getAll());
    }

    

    @Test
    @DisplayName("update: Should update position when position exists")
    void update() {
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        when(positionMapper.map(position)).thenReturn(positionDTO);

        PositionDTO updatedDTO = new PositionDTO();
        updatedDTO.setName("Updated Manager");
        updatedDTO.setDescription("Updated description");

        PositionDTO result = positionService.update(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("Updated Manager", position.getName());
        assertEquals("Updated description", position.getDescription());
        
        verify(positionRepository).findById(1L);
        verify(positionRepository).save(position);
        verify(positionMapper).map(position);
    }

    @Test
    @DisplayName("update: Should throw exception when position not found")
    void updateThrowsExceptionWhenNotFound() {
        when(positionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PositionNotFoundException.class, () -> positionService.update(1L, positionDTO));
    }

    @Test
    @DisplayName("update: Should throw exception when name is too short")
    void updateThrowsExceptionForShortName() {
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        positionDTO.setName("A");
        assertThrows(IllegalArgumentException.class, () -> positionService.update(1L, positionDTO));
    }

    @Test
    @DisplayName("update: Should throw exception when description is too long")
    void updateThrowsExceptionForLongDescription() {
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        positionDTO.setDescription("A".repeat(256));
        assertThrows(IllegalArgumentException.class, () -> positionService.update(1L, positionDTO));
    }
}
