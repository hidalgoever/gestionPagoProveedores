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
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;


@RestController
@RequestMapping("/proveedores")
@RequiredArgsConstructor
public class ProveedorController {
    
    private final IProveedorService service;
    private final ModelMapper mapper;

    @GetMapping("/pageableAll")    
    @Operation(summary = "getProveedores", operationId = "getProveedores", description = "Obtiene una lista de proveedores con paginación")
    public ResponseEntity<Page<ProveedorDTO>> getProveedores(Pageable pageable) {
         Page<ProveedorDTO> page = service.findAll(pageable).map(this::convertToDto);

         return ResponseEntity.ok(page);       
    }

    @GetMapping("/pageableByEstado/{idEstado}")
    @Operation(summary = "getProveedoresPorEstado", description = "Obtiene proveedores filtrados por estado con paginación")
    public ResponseEntity<Page<ProveedorDTO>> getProveedoresPorEstado(@PathVariable("idEstado") Integer idEstado, Pageable pageable) {
        Page<ProveedorDTO> page = service.findByEstado(idEstado, pageable).map(this::convertToDto);
        return ResponseEntity.ok(page);
    }
    

    @GetMapping("/{id}")
    @Operation(summary = "getProveedor", description = "Obtiene un proveedor por su id")
    public ResponseEntity<ProveedorDTO> getProveedor(@PathVariable("id") Integer id) {
        Proveedor obj = service.findById(id);

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PostMapping
    @Operation(summary = "guardarProveedor", description = "Crea un nuevo proveedor")
    public ResponseEntity<Void> guadarProveedor(@Valid @RequestBody ProveedorDTO dto) {
        Proveedor obj = service.save(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdProveedor()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping({"/{idProveedor}"})
    @Operation(summary = "actualizarProveedor", description = "Actualiza un proveedor existente")
    public ResponseEntity<ProveedorDTO> actualizarProveedor(@Valid @PathVariable("idProveedor") Integer id, @RequestBody ProveedorDTO dto) {
        Proveedor obj = service.update(id, convertToEntity(dto));

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PutMapping("/change-estado/{idProveedor}/{idEstado}")
    @Operation(summary = "changeEstadoProveedor", description = "Cambia el estado de un proveedor")
    public ResponseEntity<ProveedorDTO> changeEstado(@PathVariable("idProveedor") Integer id, @PathVariable("idEstado") Integer idEstado) {
        Proveedor obj = service.changeEstado(id, idEstado);

        return ResponseEntity.ok(convertToDto(obj));
    }

    private ProveedorDTO convertToDto(Proveedor obj) {
        return mapper.map(obj, ProveedorDTO.class);
    }

    private Proveedor convertToEntity(ProveedorDTO dto) {
        return mapper.map(dto, Proveedor.class);
    }   
}
