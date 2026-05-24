package com.ginko.service.impl;

import com.ginko.exception.DuplicateResourceException;
import com.ginko.model.Estado;
import com.ginko.model.Proveedor;
import com.ginko.repo.IProveedorRepo;
import com.ginko.repo.IEstadoRepo;
import com.ginko.repo.IGenericRepo;
import com.ginko.service.IProveedorService;
import com.ginko.util.TipoEstadoValidar;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl extends CRUDImpl<Proveedor, Integer> implements IProveedorService {

    private final IProveedorRepo repo;
    private final IEstadoRepo estadoRepo;     

    @Override
    protected IGenericRepo<Proveedor, Integer> getRepo() {
        return repo;
    }

    public void validateUniqueIdentificacionTributaria(Proveedor proveedor) {
        if (repo.existsByIdentificacionTributaria(proveedor.getIdentificacionTributaria())) {
            throw new DuplicateResourceException("Identificación tributaria ya registrada: " + proveedor.getIdentificacionTributaria());
        }
    }

    public Estado findById(Proveedor proveedor) {
        Estado est = estadoRepo.findById(proveedor.getEstado().getIdEstado()).orElseThrow(() -> new com.ginko.exception.ModelNotFoundException("Estado no encontrado: " + proveedor.getEstado().getIdEstado()));
        
        return est;
    }

    public Estado findByIdEstadoAndTipoEstadoIdTipoEstado(Integer idEstado, Integer tipoEstado) {
        return estadoRepo.findByIdEstadoAndTipoEstadoIdTipoEstado(idEstado, tipoEstado).orElseThrow(() -> new com.ginko.exception.ModelNotFoundException("Estado no encontrado: " + idEstado));
    }  

    @Override
    public Proveedor save(Proveedor proveedor) {
        validateUniqueIdentificacionTributaria(proveedor);      
        return  repo.save(proveedor);        
    }

    @Override
    public Proveedor update(Integer id, Proveedor proveedor) {
        Proveedor resultProveedor = findById(id);
        
        if (!resultProveedor.getIdentificacionTributaria().equals(proveedor.getIdentificacionTributaria())) {
            validateUniqueIdentificacionTributaria(proveedor);  
        }
        Estado est = findById(proveedor);        
        resultProveedor.setNombre(proveedor.getNombre());
        resultProveedor.setIdentificacionTributaria(proveedor.getIdentificacionTributaria());
        resultProveedor.setCorreoElectronico(proveedor.getCorreoElectronico());
        resultProveedor.setEstado(est);

        return repo.save(resultProveedor);
       
    }

    @Override
    public Page<Proveedor> findAll(Pageable pageable) {
        return repo.findAll(pageable);        
    }

    @Override
    public Page<Proveedor> findByEstado(Integer idEstado, Pageable pageable) {
        return repo.findByEstadoIdEstado(idEstado, pageable);        
    }

    @Override
    public Proveedor findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new com.ginko.exception.ModelNotFoundException("Proveedor no encontrado: " + id));
    }

    @Override
    public Proveedor changeEstado(Integer idProveedor, Integer idEstado) {        
        Proveedor p = findById(idProveedor);
        Estado est = findByIdEstadoAndTipoEstadoIdTipoEstado(idEstado, TipoEstadoValidar.PROVEEDOR.getId());
        p.setEstado(est);
        
        return repo.save(p);
    }

    
}
