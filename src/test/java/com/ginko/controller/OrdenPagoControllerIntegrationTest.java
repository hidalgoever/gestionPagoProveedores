package com.ginko.controller;


import com.ginko.dto.OrdenPagoDTO;
import com.ginko.dto.ProveedorDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ginko.util.EstadoValidar;
import com.ginko.util.TipoEstadoValidar;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OrdenPagoControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void createAndGetOrdenPago_flow() {
        // Preparar datos de dominio requeridos (TipoEstado y Estados con IDs esperados)
        // Insertar directamente con JdbcTemplate para fijar IDs esperados
        jdbcTemplate.update("INSERT INTO tipo_estado (id_tipo_estado, nombre) VALUES (?, ?)", TipoEstadoValidar.ORDENPAGO.getId(), "ORDEN");
        jdbcTemplate.update("INSERT INTO tipo_estado (id_tipo_estado, nombre) VALUES (?, ?)", TipoEstadoValidar.PROVEEDOR.getId(), "PROVEEDOR");

        jdbcTemplate.update("INSERT INTO estado (id_estado, nombre, id_tipo_estado) VALUES (?, ?, ?)", EstadoValidar.BORRADOR.getId(), "BORRADOR", TipoEstadoValidar.ORDENPAGO.getId());
        jdbcTemplate.update("INSERT INTO estado (id_estado, nombre, id_tipo_estado) VALUES (?, ?, ?)", EstadoValidar.ACTIVO.getId(), "ACTIVO", TipoEstadoValidar.PROVEEDOR.getId());

        Integer idEstadoProveedor = EstadoValidar.ACTIVO.getId();
        Integer expectedOrdenEstado = EstadoValidar.BORRADOR.getId();

        // 3) Crear Proveedor
        ProveedorDTO proveedor = new ProveedorDTO(null, "Prov Test", "RUC123", "p@test.com", idEstadoProveedor);
        ResponseEntity<String> provResp = restTemplate.postForEntity("/proveedores", proveedor, String.class);
        if (provResp.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("/proveedores response body: " + provResp.getBody());
        }
        assertThat(provResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Integer idProveedor = extractIdFromLocation(provResp.getHeaders().getLocation().toString());

        // 4) Crear Orden de Pago
        // No enviar estado inicial (null) para que el servicio asigne BORRADOR automáticamente
        OrdenPagoDTO orden = new OrdenPagoDTO(null, idProveedor, 100.0, "Pago prueba", null, EstadoValidar.BORRADOR.getId());
        ResponseEntity<String> ordenResp = restTemplate.postForEntity("/ordenes", orden, String.class);
        if (ordenResp.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("/ordenes response body: " + ordenResp.getBody());
        }
        assertThat(ordenResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Integer idOrden = extractIdFromLocation(ordenResp.getHeaders().getLocation().toString());

        // 5) Obtener Orden y validar
        ResponseEntity<OrdenPagoDTO> getResp = restTemplate.getForEntity("/ordenes/" + idOrden, OrdenPagoDTO.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrdenPagoDTO body = getResp.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getIdOrdenPago()).isEqualTo(idOrden);
        assertThat(body.getIdProveedor()).isEqualTo(idProveedor);
        assertThat(body.getMonto()).isEqualTo(100.0);
        assertThat(body.getConcepto()).isEqualTo("Pago prueba");
        assertThat(body.getIdEstado()).isEqualTo(expectedOrdenEstado);
    }

    private Integer extractIdFromLocation(String location) {
        String[] parts = location.split("/");
        return Integer.valueOf(parts[parts.length - 1]);
    }
}
