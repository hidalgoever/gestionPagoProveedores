package com.ginko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenPagoDTO {

    private Integer idOrdenPago;

    @NotNull(message = "El idProveedor es requerido")
    private Integer idProveedor;

    @NotNull(message = "El monto es requerido")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor que cero")
    private Double monto;

    @NotBlank(message = "El concepto es requerido")
    @Size(max = 250, message = "El concepto no puede tener más de 250 caracteres")
    private String concepto;

    //@NotNull(message = "La fechaCreacion es requerida")
    private LocalDateTime fechaCreacion;

    @NotNull(message = "El idEstado es requerido")
    private Integer idEstado;
}
