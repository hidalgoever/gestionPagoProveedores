package com.ginko.util;

public enum EstadoValidar {

    ACTIVO (5),
    INACTIVO(6),
    APROBADA(2),
    BORRADOR(1),
    RECHAZADA(3),
    PAGADA(4);

    private final Integer id;

    EstadoValidar(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
