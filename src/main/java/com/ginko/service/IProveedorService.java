package com.ginko.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ginko.model.Proveedor;

public interface IProveedorService extends ICRUD<Proveedor, Integer> {

    Page<Proveedor> findAll(Pageable pageable);

    Page<Proveedor> findByEstado(Integer idEstado, Pageable pageable);    

    Proveedor changeEstado(Integer idProveedor, Integer idEstado);
}
