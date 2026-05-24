package com.ginko.repo;

import com.ginko.model.OrdenPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrdenPagoRepo extends IGenericRepo<OrdenPago, Integer> {
    Page<OrdenPago> findByEstadoIdEstadoAndProveedorIdProveedor(Integer idEstado, Integer idProveedor, Pageable pageable);
    Page<OrdenPago> findAll(Pageable pageable);
}
