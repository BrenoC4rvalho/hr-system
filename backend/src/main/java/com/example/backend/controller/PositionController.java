package com.example.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.CreatePositionDTO;
import com.example.backend.dto.PositionDTO;
import com.example.backend.service.PositionService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/positions")
public class PositionController {
    
    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping()  
    public ResponseEntity<?> index() {

        List<PositionDTO> positions = positionService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(positions);

    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody CreatePositionDTO createPositionDTO) {
        PositionDTO newPosition = positionService.create(createPositionDTO);
    
        return ResponseEntity.status(HttpStatus.CREATED).body(newPosition);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable("id") Long id) {

        PositionDTO position = positionService.getPosition(id);
        return ResponseEntity.status(HttpStatus.OK).body(position);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PositionDTO positionDTO) {
        
        positionService.update(id, positionDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Position updated successfully.");
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
