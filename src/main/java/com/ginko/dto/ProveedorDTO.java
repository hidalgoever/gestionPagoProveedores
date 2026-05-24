package com.ginko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorDTO {

    private Integer idProveedor;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @NotBlank(message = "La identificación tributaria es requerida")
    @Size(max = 50, message = "La identificación tributaria no puede tener más de 50 caracteres")
    private String identificacionTributaria;

    @NotBlank(message = "El correo electrónico es requerido")
    @Email(message = "El correo electrónico no tiene un formato válido")
    @Size(max = 100, message = "El correo electrónico no puede tener más de 100 caracteres")
    private String correoElectronico;

    @NotNull(message = "El idEstado es requerido")
    private Integer idEstado;
}
