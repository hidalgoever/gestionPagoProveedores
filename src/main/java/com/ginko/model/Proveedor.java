package com.ginko.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proveedor", uniqueConstraints = {@UniqueConstraint(columnNames = {"identificacion_tributaria"})})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor", nullable = false)
    private Integer idProveedor;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "identificacion_tributaria", nullable = false, length = 50, unique = true)
    private String identificacionTributaria;

    @Column(name = "correo_electronico", nullable = false, length = 100)
    private String correoElectronico;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;
    
}
