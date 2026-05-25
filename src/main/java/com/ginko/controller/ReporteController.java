package com.ginko.controller;

import com.ginko.dto.OrdenPagoProximoVencerDTO;
import com.ginko.dto.ReporteTotalPagadoDTO;
import com.ginko.service.IReporteService;
import com.ginko.util.Utilidades;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reportesOrdenP")
@Validated
@RequiredArgsConstructor
public class ReporteController {

    private final IReporteService reporteService;

    @GetMapping("/totalPagadoProveedor")
    @Operation(summary = "totalPagadoProveedor", description = "Calcula el total pagado al proveedor en el rango de fechas indicado")
    public ResponseEntity<ReporteTotalPagadoDTO> totalPagadoProveedor(
            @RequestParam Integer idProveedor,
            @RequestParam @NotBlank(message = "fechaInicio es requerida") String fechaInicio,
            @RequestParam @NotBlank(message = "fechaFin es requerida") String fechaFin) {

        LocalDateTime inicio = Utilidades.parseFecha(fechaInicio, false);
        LocalDateTime fin = Utilidades.parseFecha(fechaFin, true);        

        ReporteTotalPagadoDTO resultado = reporteService.totalPagadoProveedor(idProveedor, inicio, fin);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/ordenesProximasVencer")
    @Operation(summary = "ordenesProximasVencer", description = "Obtiene órdenes de pago próximas a vencer en los próximos 3 días hábiles")
    public ResponseEntity<List<OrdenPagoProximoVencerDTO>> obtenerOrdenesProximasAVencer() {
        List<OrdenPagoProximoVencerDTO> resultado = reporteService.obtenerOrdenesProximasAVencer();
        return ResponseEntity.ok(resultado);
    }
    
}
