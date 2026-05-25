package com.ginko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteTotalPagadoDTO {

    private Integer idProveedor;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Double totalPagado;
}
