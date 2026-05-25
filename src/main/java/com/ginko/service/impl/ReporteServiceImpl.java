package com.ginko.service.impl;

import com.ginko.dto.OrdenPagoProximoVencerDTO;
import com.ginko.dto.ReporteTotalPagadoDTO;
import com.ginko.exception.ModelNotFoundException;
import com.ginko.model.OrdenPago;
import com.ginko.model.Proveedor;
import com.ginko.repo.IOrdenPagoRepo;
import com.ginko.repo.IProveedorRepo;
import com.ginko.service.IReporteService;
import com.ginko.util.EstadoValidar;
import com.ginko.util.Utilidades;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements IReporteService {

    private final IProveedorRepo proveedorRepo;
    private final IOrdenPagoRepo ordenPagoRepo;
    private final ModelMapper mapper;

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

    @Override
    public List<OrdenPagoProximoVencerDTO> obtenerOrdenesProximasAVencer() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime finRango = Utilidades.calcularFechaFinHabil(ahora.toLocalDate(), 3);

        List<OrdenPago> ordenes = ordenPagoRepo.findProximasAVencer("PENDIENTE");

        return mapearAResponse(ordenes, ahora, finRango);
    }

    private List<OrdenPagoProximoVencerDTO> mapearAResponse(List<OrdenPago> ordenes, LocalDateTime inicio, LocalDateTime finRango) {
        TypeMap<OrdenPago, OrdenPagoProximoVencerDTO> typeMap = mapper.getTypeMap(OrdenPago.class, OrdenPagoProximoVencerDTO.class);
        if (typeMap == null) {
            typeMap = mapper.createTypeMap(OrdenPago.class, OrdenPagoProximoVencerDTO.class);
            typeMap.addMappings(map -> {
                map.map(OrdenPago::getIdOrdenPago, OrdenPagoProximoVencerDTO::setIdOrdenPago);
                map.map(src -> src.getProveedor().getIdProveedor(), OrdenPagoProximoVencerDTO::setIdProveedor);
                map.map(src -> src.getProveedor().getNombre(), OrdenPagoProximoVencerDTO::setNombreProveedor);
                map.map(OrdenPago::getMonto, OrdenPagoProximoVencerDTO::setMonto);
                map.map(OrdenPago::getConcepto, OrdenPagoProximoVencerDTO::setConcepto);
                map.map(src -> src.getEstado().getNombre(), OrdenPagoProximoVencerDTO::setEstado);
            });
        }

        List<OrdenPagoProximoVencerDTO> response = new ArrayList<>();

        for (OrdenPago orden : ordenes) {
            LocalDateTime fechaVencimiento = Utilidades.calcularFechaVencimiento(orden.getFechaCreacion(), 3);
            if (!fechaVencimiento.isBefore(inicio) && !fechaVencimiento.isAfter(finRango)) {
                OrdenPagoProximoVencerDTO dto = mapper.map(orden, OrdenPagoProximoVencerDTO.class);
                dto.setFechaVencimiento(fechaVencimiento);
                response.add(dto);
            }
        }

        return response;
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
