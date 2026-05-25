package com.ginko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenPagoProximoVencerDTO {

    private Integer idOrdenPago;
    private Integer idProveedor;
    private String nombreProveedor;
    private Double monto;
    private String concepto;
    private LocalDateTime fechaVencimiento;
    private String estado;
}
