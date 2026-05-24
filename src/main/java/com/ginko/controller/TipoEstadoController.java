package com.ginko.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.ginko.dto.TipoEstadoDTO;
import com.ginko.model.TipoEstado;
import com.ginko.service.ITipoEstadoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tipoEstados")
@RequiredArgsConstructor
public class TipoEstadoController {
    
    private final ITipoEstadoService service;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<TipoEstadoDTO>> findAll() {        
        List<TipoEstadoDTO> list = service.findAll().stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoEstadoDTO> findById(@PathVariable("id") Integer id) {
        TipoEstado obj = service.findById(id);

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody TipoEstadoDTO dto) {
        TipoEstado obj = service.save(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdTipoEstado()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoEstadoDTO> update(@Valid @PathVariable("id") Integer id, @RequestBody TipoEstadoDTO dto) {
        TipoEstado obj = service.update(id, convertToEntity(dto));

        return ResponseEntity.ok(convertToDto(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    private TipoEstadoDTO convertToDto(TipoEstado obj) {
        return mapper.map(obj, TipoEstadoDTO.class);
    }

    private TipoEstado convertToEntity(TipoEstadoDTO dto) {
        return mapper.map(dto, TipoEstado.class);
    }   
}
