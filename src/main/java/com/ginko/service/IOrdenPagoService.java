package com.ginko.service;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ginko.model.OrdenPago;

public interface IOrdenPagoService extends ICRUD<OrdenPago, Integer> {    

    Page<OrdenPago> findByEstadoAndProveedor(Integer idEstado, Integer idProveedor, Pageable pageable);
    Page<OrdenPago> findAll(Pageable pageable);
    OrdenPago changeEstado(Integer idOrdenPago, Integer idEstado);    
    List<OrdenPago> findOrdenesByFechaVigenciaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);   
}
