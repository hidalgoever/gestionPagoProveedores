package com.ginko.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ginko.model.TipoEstado;
import com.ginko.repo.IGenericRepo;
import com.ginko.repo.ITipoEstadoRepo;
import com.ginko.service.ITipoEstadoService;

@Service
@RequiredArgsConstructor
public class TipoEstadoServiceImpl extends CRUDImpl<TipoEstado, Integer> implements ITipoEstadoService {
    
    private final ITipoEstadoRepo repo;

    @Override
    protected IGenericRepo<TipoEstado, Integer> getRepo() {
        return repo;
    }
}
