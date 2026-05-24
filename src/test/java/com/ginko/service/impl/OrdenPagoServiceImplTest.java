package com.ginko.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ginko.exception.ModelNotFoundException;
import com.ginko.model.Estado;
import com.ginko.model.OrdenPago;
import com.ginko.model.Proveedor;
import com.ginko.repo.IOrdenPagoRepo;
import com.ginko.repo.IProveedorRepo;
import com.ginko.repo.IEstadoRepo;
import com.ginko.util.EstadoValidar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de OrdenPagoServiceImpl")
class OrdenPagoServiceImplTest {

    @Mock
    private IOrdenPagoRepo ordenPagoRepo;

    @Mock
    private IProveedorRepo proveedorRepo;

    @Mock
    private IEstadoRepo estadoRepo;

    @InjectMocks
    private OrdenPagoServiceImpl ordenPagoService;

    private OrdenPago ordenPago;
    private Proveedor proveedor;
    private Estado estadoActivo;
    private Estado estadoBorrador;

    @BeforeEach
    void setUp() {
        // Configurar estados
        estadoActivo = new Estado();
        estadoActivo.setIdEstado(EstadoValidar.ACTIVO.getId());
        estadoActivo.setNombre("ACTIVO");

        estadoBorrador = new Estado();
        estadoBorrador.setIdEstado(EstadoValidar.BORRADOR.getId());
        estadoBorrador.setNombre("BORRADOR");

        // Configurar proveedor
        proveedor = new Proveedor();
        proveedor.setIdProveedor(1);
        proveedor.setNombre("Proveedor Test");
        proveedor.setIdentificacionTributaria("1234567890");
        proveedor.setCorreoElectronico("proveedor@test.com");
        proveedor.setEstado(estadoActivo);

        // Configurar orden de pago
        ordenPago = new OrdenPago();
        ordenPago.setIdOrdenPago(1);
        ordenPago.setProveedor(proveedor);
        ordenPago.setMonto(1000.00);
        ordenPago.setConcepto("Pago por servicios");
        ordenPago.setFechaCreacion(LocalDateTime.now());
        ordenPago.setEstado(estadoBorrador);
    }

    @Test
    @DisplayName("Guardar orden de pago exitosamente")
    void testGuardarOrdenPago() {
        // Arrange
        when(proveedorRepo.findById(1)).thenReturn(Optional.of(proveedor));
        when(estadoRepo.findById(EstadoValidar.BORRADOR.getId())).thenReturn(Optional.of(estadoBorrador));
        when(ordenPagoRepo.save(ordenPago)).thenReturn(ordenPago);

        // Act
        OrdenPago resultado = ordenPagoService.save(ordenPago);

        // Assert
        assertNotNull(resultado);
        assertEquals(1000.00, resultado.getMonto());
        verify(ordenPagoRepo, times(1)).save(ordenPago);
    }

    @Test
    @DisplayName("Guardar orden de pago con proveedor inactivo")
    void testGuardarOrdenPagoProveedorInactivo() {
        // Arrange
        Estado estadoInactivo = new Estado();
        estadoInactivo.setIdEstado(EstadoValidar.INACTIVO.getId());
        estadoInactivo.setNombre("INACTIVO");
        proveedor.setEstado(estadoInactivo);

        when(proveedorRepo.findById(1)).thenReturn(Optional.of(proveedor));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            ordenPagoService.save(ordenPago);
        });
        verify(ordenPagoRepo, never()).save(ordenPago);
    }

    @Test
    @DisplayName("Actualizar orden de pago exitosamente")
    void testActualizarOrdenPago() {
        // Arrange
        OrdenPago ordenActualizada = new OrdenPago();
        ordenActualizada.setMonto(2000.00);
        ordenActualizada.setConcepto("Pago actualizado");
        ordenActualizada.setFechaCreacion(LocalDateTime.now());

        Estado estadoAprobada = new Estado();
        estadoAprobada.setIdEstado(EstadoValidar.APROBADA.getId());
        estadoAprobada.setNombre("APROBADA");
        ordenActualizada.setEstado(estadoAprobada);

        when(ordenPagoRepo.findById(1)).thenReturn(Optional.of(ordenPago));
        when(estadoRepo.findById(EstadoValidar.APROBADA.getId())).thenReturn(Optional.of(estadoAprobada));
        when(ordenPagoRepo.save(any(OrdenPago.class))).thenReturn(ordenActualizada);

        // Act
        OrdenPago resultado = ordenPagoService.update(1, ordenActualizada);

        // Assert
        assertNotNull(resultado);
        assertEquals(2000.00, resultado.getMonto());
        verify(ordenPagoRepo, times(1)).save(any(OrdenPago.class));
    }

    @Test
    @DisplayName("Transición de BORRADOR a APROBADA")
    void testTransicionBorradorAAprobada() {
        // Arrange
        Estado estadoAprobada = new Estado();
        estadoAprobada.setIdEstado(EstadoValidar.APROBADA.getId());
        estadoAprobada.setNombre("APROBADA");

        when(ordenPagoRepo.findById(1)).thenReturn(Optional.of(ordenPago));
        when(estadoRepo.findById(EstadoValidar.APROBADA.getId())).thenReturn(Optional.of(estadoAprobada));
        when(ordenPagoRepo.save(any(OrdenPago.class))).thenReturn(ordenPago);

        // Act
        OrdenPago resultado = ordenPagoService.changeEstado(1, EstadoValidar.APROBADA.getId());

        // Assert
        assertNotNull(resultado);
        verify(ordenPagoRepo, times(1)).save(any(OrdenPago.class));
    }

    @Test
    @DisplayName("Transición inválida de BORRADOR a PAGADA (debe ser APROBADA antes)")
    void testTransicionInvalidaBorradorAPagada() {
        // Arrange
        Estado estadoPagada = new Estado();
        estadoPagada.setIdEstado(EstadoValidar.PAGADA.getId());
        estadoPagada.setNombre("PAGADA");

        when(ordenPagoRepo.findById(1)).thenReturn(Optional.of(ordenPago));
        when(estadoRepo.findById(EstadoValidar.PAGADA.getId())).thenReturn(Optional.of(estadoPagada));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            ordenPagoService.changeEstado(1, EstadoValidar.PAGADA.getId());
        });
    }

    @Test
    @DisplayName("Cambiar estado de orden no encontrada")
    void testCambiarEstadoOrdenNoEncontrada() {
        // Arrange
        when(ordenPagoRepo.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ModelNotFoundException.class, () -> {
            ordenPagoService.changeEstado(999, EstadoValidar.APROBADA.getId());
        });
    }

    @Test
    @DisplayName("Transición de APROBADA a PAGADA")
    void testTransicionAprobadaAPagada() {
        // Arrange
        Estado estadoAprobada = new Estado();
        estadoAprobada.setIdEstado(EstadoValidar.APROBADA.getId());
        estadoAprobada.setNombre("APROBADA");
        ordenPago.setEstado(estadoAprobada);

        Estado estadoPagada = new Estado();
        estadoPagada.setIdEstado(EstadoValidar.PAGADA.getId());
        estadoPagada.setNombre("PAGADA");

        when(ordenPagoRepo.findById(1)).thenReturn(Optional.of(ordenPago));
        when(estadoRepo.findById(EstadoValidar.PAGADA.getId())).thenReturn(Optional.of(estadoPagada));
        when(ordenPagoRepo.save(any(OrdenPago.class))).thenReturn(ordenPago);

        // Act
        OrdenPago resultado = ordenPagoService.changeEstado(1, EstadoValidar.PAGADA.getId());

        // Assert
        assertNotNull(resultado);
        verify(ordenPagoRepo, times(1)).save(any(OrdenPago.class));
    }

    @Test
    @DisplayName("Guardar orden de pago con proveedor no encontrado")
    void testGuardarOrdenPagoProveedorNoEncontrado() {
        // Arrange
        when(proveedorRepo.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ModelNotFoundException.class, () -> {
            ordenPagoService.save(ordenPago);
        });
    }
}
