package com.ginko.repo;

import com.ginko.model.OrdenPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrdenPagoRepo extends IGenericRepo<OrdenPago, Integer> {
    Page<OrdenPago> findByEstadoIdEstadoAndProveedorIdProveedor(Integer idEstado, Integer idProveedor, Pageable pageable);
    Page<OrdenPago> findAll(Pageable pageable);

    @Query("SELECT SUM(o.monto) FROM OrdenPago o WHERE o.proveedor.idProveedor = :idProveedor AND o.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    Double totalPagadoByProveedorAndFechaCreacionBetween(@Param("idProveedor") Integer idProveedor,
                                                           @Param("fechaInicio") LocalDateTime fechaInicio,
                                                           @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT o FROM OrdenPago o WHERE o.estado.nombre = :estadoPendiente AND o.estado.nombre <> 'CANCELADA'")
    List<OrdenPago> findProximasAVencer(@Param("estadoPendiente") String estadoPendiente);

    List<OrdenPago> findByFechaVigenciaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

}
