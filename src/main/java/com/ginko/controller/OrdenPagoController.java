package com.ginko.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.ginko.dto.OrdenPagoDTO;
import com.ginko.model.OrdenPago;
import com.ginko.service.IOrdenPagoService;
import java.net.URI;



@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenPagoController {
    
    private final IOrdenPagoService service;
    private final ModelMapper mapper;

    @GetMapping("/pageableAll")
    public ResponseEntity<Page<OrdenPagoDTO>> getOrdenesPago(Pageable pageable) {
         Page<OrdenPagoDTO> page = service.findAll(pageable).map(this::convertToDto);

         return ResponseEntity.ok(page);       
    }

    @GetMapping("/pageableParameter/{idEstado}/{idProveedor}")
    public ResponseEntity<Page<OrdenPagoDTO>> getOrdenesPagoPorEstadoProveedor(@PathVariable("idEstado") Integer idEstado,@PathVariable("idProveedor") Integer idProveedor, Pageable pageable) {
        Page<OrdenPagoDTO> page = service.findByEstadoAndProveedor(idEstado,idProveedor,pageable).map(this::convertToDto);
        
        return ResponseEntity.ok(page);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<OrdenPagoDTO> getOrdenPago(@PathVariable("id") Integer id) {
        OrdenPago obj = service.findById(id);

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PostMapping
    public ResponseEntity<Void> saveOrdenPago(@Valid @RequestBody OrdenPagoDTO dto) {
        OrdenPago obj = service.save(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdOrdenPago()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping({"/{idOrdenPago}"})
    public ResponseEntity<OrdenPagoDTO> updateOrdenPago(@Valid @PathVariable("idOrdenPago") Integer id, @RequestBody OrdenPagoDTO dto) {
        OrdenPago obj = service.update(id, convertToEntity(dto));

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PutMapping("/change-estado/{idOrdenPago}/{idEstado}")
    public ResponseEntity<OrdenPagoDTO> changeEstado(@PathVariable("idOrdenPago") Integer id, @PathVariable("idEstado") Integer idEstado) {
        OrdenPago obj = service.changeEstado(id, idEstado);

        return ResponseEntity.ok(convertToDto(obj));
    }    

    private OrdenPagoDTO convertToDto(OrdenPago obj) {
        return mapper.map(obj, OrdenPagoDTO.class);
    }

    private OrdenPago convertToEntity(OrdenPagoDTO dto) {
        return mapper.map(dto, OrdenPago.class);
    }   
}
