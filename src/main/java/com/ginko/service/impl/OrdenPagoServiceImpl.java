package com.ginko.service.impl;

import com.ginko.model.Estado;
import com.ginko.model.OrdenPago;
import com.ginko.model.Proveedor;
import com.ginko.repo.IOrdenPagoRepo;
import com.ginko.repo.IProveedorRepo;
import com.ginko.repo.IEstadoRepo;
import com.ginko.repo.IGenericRepo;
import com.ginko.service.IOrdenPagoService;
import com.ginko.util.EstadoValidar;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrdenPagoServiceImpl extends CRUDImpl<OrdenPago, Integer> implements IOrdenPagoService {

    private static final Logger logger = LoggerFactory.getLogger(OrdenPagoServiceImpl.class);

    private final IOrdenPagoRepo repo;
    private final IProveedorRepo proveedorRepo;
    private final IEstadoRepo estadoRepo;    
    
    @Override
    protected IGenericRepo<OrdenPago, Integer> getRepo() {
        return repo;
    }      

    public void validateProveedorActivo(OrdenPago orden) {       
        Proveedor prov = proveedorRepo.findById(orden.getProveedor().getIdProveedor()).orElseThrow(() -> new com.ginko.exception.ModelNotFoundException("Proveedor no encontrado: " + orden.getProveedor().getIdentificacionTributaria()));
        if (!EstadoValidar.ACTIVO.getId().equals(prov.getEstado().getIdEstado())) {
            throw new IllegalStateException("Proveedor no está ACTIVO: " + prov.getIdentificacionTributaria());
        }
    }  

    public OrdenPago validateEstadoInicialOrdenPago(OrdenPago orden) {
        if (orden.getEstado() == null) {
            Estado borrador = estadoRepo.findById(EstadoValidar.BORRADOR.getId()).orElseThrow(() -> new com.ginko.exception.ModelNotFoundException("Estado BORRADOR no configurado"));
            orden.setEstado(borrador);

        } else {
            Estado est = estadoRepo.findById(orden.getEstado().getIdEstado()).orElseThrow(() -> new com.ginko.exception.ModelNotFoundException("Estado no encontrado: " + orden.getEstado().getIdEstado()));
            if(!est.getIdEstado().equals(EstadoValidar.BORRADOR.getId())){
                throw new IllegalArgumentException("No se puede asignar un estado inicial diferente de BORRADOR a la orden");
            }
            orden.setEstado(est);       
        }

        return orden;
    }

    public Estado findByIdEstado(OrdenPago orden){
        return estadoRepo.findById(orden.getEstado().getIdEstado()).orElseThrow(() -> new com.ginko.exception.ModelNotFoundException("Estado no encontrado: " + orden.getEstado().getIdEstado()));
    }

    public Estado transitionEstado(Integer idOrdenPago, Integer idNuevoEstado) {
        OrdenPago orden = findById(idOrdenPago);
        OrdenPago ordenEstadoNuevo = new OrdenPago();
        Estado et = new Estado();
        et.setIdEstado(idNuevoEstado);
        ordenEstadoNuevo.setEstado(et);
        Estado nuevo = findByIdEstado(ordenEstadoNuevo);
        Integer actual = orden.getEstado().getIdEstado();
        Integer dest = idNuevoEstado;
        String nombreEstadoActual = orden.getEstado().getNombre();
        String nombreEstadoNuevo = nuevo.getNombre();
        boolean ok = false;
        if (EstadoValidar.BORRADOR.getId().equals(actual)&& (EstadoValidar.APROBADA.getId().equals(dest) || EstadoValidar.RECHAZADA.getId().equals(dest))) ok = true;
        if (EstadoValidar.APROBADA.getId().equals(actual) && EstadoValidar.PAGADA.getId().equals(dest)) ok = true;
        if (!ok) {
            String msg = "Transición inválida de " + nombreEstadoActual + " a " + nombreEstadoNuevo;
            logger.warn(msg);
            throw new IllegalStateException(msg);
        }

        logger.info("Transición orden {}: {} -> {}", idOrdenPago, nombreEstadoActual, nombreEstadoNuevo);
        return nuevo; 
    }
    

    @Override
    public OrdenPago save(OrdenPago orden) {
        validateProveedorActivo(orden);
        validateEstadoInicialOrdenPago(orden);
        return repo.save(orden);        
    }

    @Override
    public OrdenPago update(Integer id, OrdenPago orden) {

        OrdenPago existing = findById(id); 
        existing.setMonto(orden.getMonto());
        existing.setConcepto(orden.getConcepto());
        existing.setFechaCreacion(orden.getFechaCreacion());       
        existing.setEstado(transitionEstado(id, orden.getEstado().getIdEstado()));
        return  repo.save(existing);       
    }


    @Override
    public Page<OrdenPago> findByEstadoAndProveedor(Integer idEstado, Integer idProveedor, Pageable pageable) {
        return repo.findByEstadoIdEstadoAndProveedorIdProveedor(idEstado, idProveedor, pageable);        
    }

    @Override
    public OrdenPago findById(Integer id) {
        OrdenPago e = repo.findById(id).orElseThrow(() -> new com.ginko.exception.ModelNotFoundException("Orden no encontrada: " + id));
        return e;
    }

    @Override
    public Page<OrdenPago> findAll(Pageable pageable) {
        return repo.findAll(pageable);        
    }

    @Override
    public OrdenPago changeEstado(Integer idOrdenPago, Integer idEstado) {
        
        OrdenPago orden = findById(idOrdenPago);                     
        orden.setEstado(transitionEstado(idOrdenPago, idEstado));
        return  repo.save(orden); 
    }
    
}
