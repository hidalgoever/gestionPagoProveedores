package com.ginko.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.Operation;

import com.ginko.dto.EstadoDTO;
import com.ginko.dto.OrdenPagoDTO;
import com.ginko.model.OrdenPago;
import com.ginko.service.IOrdenPagoService;
import java.net.URI;
import java.util.List;



@RestController
@RequestMapping("/ordenes")
@RequiredArgsConstructor
public class OrdenPagoController {
    
    private final IOrdenPagoService service;
    private final ModelMapper mapper;

    @GetMapping("/pageableAll")
        @Operation(summary = "getOrdenesPago", description = "Obtiene órdenes de pago con paginación")
        public ResponseEntity<Page<OrdenPagoDTO>> getOrdenesPago(Pageable pageable) {
         Page<OrdenPagoDTO> page = service.findAll(pageable).map(this::convertToDto);

         return ResponseEntity.ok(page);       
    }

    @GetMapping("/pageableParameter/{idEstado}/{idProveedor}")
    @Operation(summary = "getOrdenesPagoPorEstadoProveedor", description = "Obtiene órdenes de pago filtradas por estado y proveedor con paginación")
    public ResponseEntity<Page<OrdenPagoDTO>> getOrdenesPagoPorEstadoProveedor(@PathVariable("idEstado") Integer idEstado,@PathVariable("idProveedor") Integer idProveedor, Pageable pageable) {
        Page<OrdenPagoDTO> page = service.findByEstadoAndProveedor(idEstado,idProveedor,pageable).map(this::convertToDto);
        
        return ResponseEntity.ok(page);
    }
    

    @GetMapping("/{id}")
    @Operation(summary = "getOrdenPago", description = "Obtiene una orden de pago por id")
    public ResponseEntity<OrdenPagoDTO> getOrdenPago(@PathVariable("id") Integer id) {
        OrdenPago obj = service.findById(id);

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PostMapping
    @Operation(summary = "guardarOrdenPago", description = "Crea una nueva orden de pago")
    public ResponseEntity<Void> guardarOrdenPago(@Valid @RequestBody OrdenPagoDTO dto) {
        OrdenPago obj = service.save(convertToEntity(dto));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdOrdenPago()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping({"/{idOrdenPago}"})
    @Operation(summary = "actualizarOrdenPago", description = "Actualiza una orden de pago existente")
    public ResponseEntity<OrdenPagoDTO> actualizarOrdenPago(@Valid @PathVariable("idOrdenPago") Integer id, @RequestBody OrdenPagoDTO dto) {
        OrdenPago obj = service.update(id, convertToEntity(dto));

        return ResponseEntity.ok(convertToDto(obj));
    }

    @PutMapping("/change-estado/{idOrdenPago}/{idEstado}")
    @Operation(summary = "changeEstadoOrdenPago", description = "Cambia el estado de una orden de pago")
    public ResponseEntity<OrdenPagoDTO> changeEstado(@PathVariable("idOrdenPago") Integer id, @PathVariable("idEstado") Integer idEstado) {
        OrdenPago obj = service.changeEstado(id, idEstado);

        return ResponseEntity.ok(convertToDto(obj));
    } 

   @GetMapping("/ordenesVigencia")
        @Operation(summary = "ordenesVigencia", description = "Obtiene órdenes de pago por fecha de vigencia entre dos fechas")
        public ResponseEntity<List<OrdenPagoDTO>> getOrdenesPago( @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        
        List<OrdenPagoDTO> list = service.findAll().stream().map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    private OrdenPagoDTO convertToDto(OrdenPago obj) {
        return mapper.map(obj, OrdenPagoDTO.class);
    }

    private OrdenPago convertToEntity(OrdenPagoDTO dto) {
        return mapper.map(dto, OrdenPago.class);
    }   
}
