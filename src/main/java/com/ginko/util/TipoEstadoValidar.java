package com.ginko.util;

public enum TipoEstadoValidar {
     PROVEEDOR (2),
     ORDENPAGO(1);   

    private final Integer id;

    TipoEstadoValidar(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
