package com.ginko.service;

import com.ginko.dto.ReporteTotalPagadoDTO;

import java.time.LocalDateTime;

public interface IReporteService {

    ReporteTotalPagadoDTO totalPagadoProveedor(Integer idProveedor, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
