package com.ginko.controller;

import com.ginko.dto.ReporteTotalPagadoDTO;
import com.ginko.service.IReporteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/reportes")
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

        LocalDateTime inicio = parseFecha(fechaInicio, false);
        LocalDateTime fin = parseFecha(fechaFin, true);        

        ReporteTotalPagadoDTO resultado = reporteService.totalPagadoProveedor(idProveedor, inicio, fin);
        return ResponseEntity.ok(resultado);
    }

    private LocalDateTime parseFecha(String valor, boolean endOfDay) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("La fecha no debe estar vacía");
        }

        try {
            if (valor.length() <= 10) {
                LocalDate fecha = LocalDate.parse(valor, DateTimeFormatter.ISO_LOCAL_DATE);
                return endOfDay ? fecha.atTime(LocalTime.MAX) : fecha.atStartOfDay();
            }
            return LocalDateTime.parse(valor, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use yyyy-MM-dd o yyyy-MM-dd'T'HH:mm:ss");
        }
    }
}
