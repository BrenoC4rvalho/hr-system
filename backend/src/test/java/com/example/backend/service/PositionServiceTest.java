package com.example.backend.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setUp() {
        position = new Position();
        position.setId(1L);
        position.setName("Manager");
        position.setDescription("Manages employees");

        positionDTO = new PositionDTO();
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
    void testCreate() {

    }

    @Test
    void testGetAll() {

    }

    

    @Test
    void testUpdate() {

    }
}
