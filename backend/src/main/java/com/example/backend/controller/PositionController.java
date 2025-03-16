package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.PositionDTO;
import com.example.backend.service.PositionService;

@RestController
@RequestMapping("/positions")
public class PositionController {
    
    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    public ResponseEntity<?> index() {

        List<PositionDTO> positions = positionService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(positions);

    }

}
