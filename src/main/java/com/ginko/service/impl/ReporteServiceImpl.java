package com.ginko.service.impl;

import com.ginko.dto.ReporteTotalPagadoDTO;
import com.ginko.exception.ModelNotFoundException;
import com.ginko.model.Proveedor;
import com.ginko.repo.IOrdenPagoRepo;
import com.ginko.repo.IProveedorRepo;
import com.ginko.service.IReporteService;
import com.ginko.util.EstadoValidar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements IReporteService {

    private final IProveedorRepo proveedorRepo;
    private final IOrdenPagoRepo ordenPagoRepo;

    @Override
    public ReporteTotalPagadoDTO totalPagadoProveedor(Integer idProveedor, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        validateParametros(idProveedor, fechaInicio, fechaFin);

        Proveedor proveedor = proveedorRepo.findById(idProveedor)
                .orElseThrow(() -> new ModelNotFoundException("Proveedor no encontrado: " + idProveedor));

        if (proveedor.getIdentificacionTributaria() == null || proveedor.getIdentificacionTributaria().isBlank()) {
            throw new IllegalStateException("Proveedor con identificacion tributaria vacía: " + idProveedor);
        }

        if (!EstadoValidar.ACTIVO.getId().equals(proveedor.getEstado().getIdEstado())) {
            throw new IllegalStateException("Proveedor no está ACTIVO: " + idProveedor);
        }

        Double total = ordenPagoRepo.totalPagadoByProveedorAndFechaCreacionBetween(idProveedor, fechaInicio, fechaFin);
        if (total == null) {
            total = 0.0;
        }

        return new ReporteTotalPagadoDTO(idProveedor, fechaInicio, fechaFin, total);
    }

    private void validateParametros(Integer idProveedor, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (idProveedor == null) {
            throw new IllegalArgumentException("idProveedor es requerido");
        }
        if (fechaInicio == null) {
            throw new IllegalArgumentException("fechaInicio es requerida");
        }
        if (fechaFin == null) {
            throw new IllegalArgumentException("fechaFin es requerida");
        }
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("fechaFin debe ser posterior a fechaInicio");
        }
    }
}
