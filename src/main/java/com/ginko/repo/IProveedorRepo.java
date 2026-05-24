package com.ginko.repo;

import com.ginko.model.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface IProveedorRepo extends IGenericRepo<Proveedor, Integer> {

    boolean existsByIdentificacionTributaria(String identificacionTributaria);

    Optional<Proveedor> findByIdentificacionTributaria(String identificacionTributaria);

    Page<Proveedor> findByEstadoIdEstado(Integer idEstado, Pageable pageable);

    Page<Proveedor> findAll(Pageable pageable);
}
