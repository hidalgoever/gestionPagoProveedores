package com.ginko.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ginko.model.Estado;
import com.ginko.repo.IEstadoRepo;
import com.ginko.repo.IGenericRepo;
import com.ginko.service.IEstadoService;

@Service
@RequiredArgsConstructor
public class EstadoServiceImpl extends CRUDImpl<Estado, Integer> implements IEstadoService {
    
    private final IEstadoRepo repo;

    @Override
    protected IGenericRepo<Estado, Integer> getRepo() {
        return repo;
    }
    
}
