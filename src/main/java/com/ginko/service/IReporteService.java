package com.ginko.service;

import com.ginko.dto.OrdenPagoProximoVencerDTO;
import com.ginko.dto.ReporteTotalPagadoDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IReporteService {

    ReporteTotalPagadoDTO totalPagadoProveedor(Integer idProveedor, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<OrdenPagoProximoVencerDTO> obtenerOrdenesProximasAVencer();
}
