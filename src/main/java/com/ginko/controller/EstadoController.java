package com.ginko.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ginko.dto.EstadoDTO;
import com.ginko.model.Estado;
import com.ginko.service.IEstadoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/estados")
@RequiredArgsConstructor
public class EstadoController {
    
    private final IEstadoService service;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<EstadoDTO>> findAll() {        
        List<EstadoDTO> list = service.findAll().stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoDTO> findById(@PathVariable("id") Integer id) {
        Estado obj = service.findById(id);

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody EstadoDTO dto) {
        Estado obj = service.save(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdEstado()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody EstadoDTO dto) {
        Estado obj = service.update(id, convertToEntity(dto));

        return ResponseEntity.ok(convertToDto(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    private EstadoDTO convertToDto(Estado obj) {
        return mapper.map(obj, EstadoDTO.class);
    }

    private Estado convertToEntity(EstadoDTO dto) {
        return mapper.map(dto, Estado.class);
    }   
}
