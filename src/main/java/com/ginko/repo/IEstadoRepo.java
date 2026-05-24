package com.ginko.repo;

import java.util.Optional;

import com.ginko.model.Estado;

public interface IEstadoRepo extends IGenericRepo<Estado, Integer> {

    Optional<Estado> findByIdEstadoAndTipoEstadoIdTipoEstado(Integer IdEstado, Integer  TipoEstadoIdTipoEstado);
}
