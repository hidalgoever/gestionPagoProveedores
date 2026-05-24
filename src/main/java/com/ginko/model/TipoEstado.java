package com.ginko.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_estado")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_estado", nullable = false)
    private Integer idTipoEstado;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;    
}
