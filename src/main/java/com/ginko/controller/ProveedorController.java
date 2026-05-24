package com.ginko.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.ginko.dto.ProveedorDTO;
import com.ginko.model.Proveedor;
import com.ginko.service.IProveedorService;
import java.net.URI;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/proveedores")
@RequiredArgsConstructor
public class ProveedorController {
    
    private final IProveedorService service;
    private final ModelMapper mapper;

    @GetMapping("/pageableAll")
    public ResponseEntity<Page<ProveedorDTO>> findAll(Pageable pageable) {
         Page<ProveedorDTO> page = service.findAll(pageable).map(this::convertToDto);

         return ResponseEntity.ok(page);       
    }

    @GetMapping("/pageableByEstado/{idEstado}")
    public ResponseEntity<Page<ProveedorDTO>> findByEstado(@PathVariable("idEstado") Integer idEstado, Pageable pageable) {
        Page<ProveedorDTO> page = service.findByEstado(idEstado, pageable).map(this::convertToDto);
        return ResponseEntity.ok(page);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> findById(@PathVariable("id") Integer id) {
        Proveedor obj = service.findById(id);

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ProveedorDTO dto) {
        Proveedor obj = service.save(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdProveedor()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping({"/{idProveedor}"})
    public ResponseEntity<ProveedorDTO> update(@Valid @PathVariable("idProveedor") Integer id, @RequestBody ProveedorDTO dto) {
        Proveedor obj = service.update(id, convertToEntity(dto));

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PutMapping("/change-estado/{idProveedor}/{idEstado}")
    public ResponseEntity<ProveedorDTO> changeEstado(@PathVariable("idProveedor") Integer id, @PathVariable("idEstado") Integer idEstado) {
        Proveedor obj = service.changeEstado(id, idEstado);

        return ResponseEntity.ok(convertToDto(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    private ProveedorDTO convertToDto(Proveedor obj) {
        return mapper.map(obj, ProveedorDTO.class);
    }

    private Proveedor convertToEntity(ProveedorDTO dto) {
        return mapper.map(dto, Proveedor.class);
    }   
}
