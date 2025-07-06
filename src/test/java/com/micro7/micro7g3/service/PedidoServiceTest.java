package com.micro7.micro7g3.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.micro7.micro7g3.model.EstadoPedido;
import com.micro7.micro7g3.model.HistorialPedido;
import com.micro7.micro7g3.model.Pedido;
import com.micro7.micro7g3.model.PedidoDetalle;
import com.micro7.micro7g3.repository.HistorialPedidoRepository;
import com.micro7.micro7g3.repository.PedidoDetalleRepository;
import com.micro7.micro7g3.repository.PedidoRepository;

public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private HistorialPedidoRepository historialPedidoRepository;

    @Mock
    private PedidoDetalleRepository pedidoDetalleRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearPedido() {
        String idUsuario = "user123";
        int idTienda = 101;

        Pedido pedidoMock = new Pedido();
        pedidoMock.setIdUsuario(idUsuario);
        pedidoMock.setIdTienda(idTienda);
        pedidoMock.setEstado(EstadoPedido.PENDIENTE);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoMock);

        Pedido resultado = pedidoService.crearPedido(idUsuario, idTienda);

        assertNotNull(resultado);
        assertEquals(EstadoPedido.PENDIENTE, resultado.getEstado());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void testCancelarPedido() {
        UUID idPedido = UUID.randomUUID();
        Pedido pedido = new Pedido();
        pedido.setIdPedido(idPedido);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedido));

        boolean resultado = pedidoService.cancelarPedido(idPedido);

        assertTrue(resultado);
        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
        verify(pedidoRepository).save(pedido);
        verify(historialPedidoRepository).save(any(HistorialPedido.class));
    }

    @Test
    void testCambiarEstadoPedido() {
        UUID idPedido = UUID.randomUUID();
        Pedido pedido = new Pedido();
        pedido.setIdPedido(idPedido);
        pedido.setEstado(EstadoPedido.PENDIENTE);

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedido));

        boolean resultado = pedidoService.cambiarEstadoPedido(idPedido, EstadoPedido.EN_PREPARACION);

        assertTrue(resultado);
        assertEquals(EstadoPedido.EN_PREPARACION, pedido.getEstado());
        verify(pedidoRepository).save(pedido);
        verify(historialPedidoRepository).save(any(HistorialPedido.class));
    }

    @Test
    void testConsultarPedido() {
        UUID idPedido = UUID.randomUUID();
        Pedido pedido = new Pedido();
        pedido.setIdPedido(idPedido);

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.consultarPedido(idPedido);

        assertNotNull(resultado);
        assertEquals(idPedido, resultado.getIdPedido());
    }

    @Test
    void testListarTodos() {
        List<Pedido> pedidos = List.of(new Pedido(), new Pedido());

        when(pedidoRepository.findAll()).thenReturn(pedidos);

        List<Pedido> resultado = pedidoService.listarTodos();

        assertEquals(2, resultado.size());
    }

    @Test
    void testListarPedidosUsuario() {
        String idUsuario = "user123";
        List<Pedido> pedidos = List.of(new Pedido(), new Pedido());

        when(pedidoRepository.findByIdUsuario(idUsuario)).thenReturn(pedidos);

        List<Pedido> resultado = pedidoService.listarPedidosUsuario(idUsuario);

        assertEquals(2, resultado.size());
    }

    @Test
    void testListarPedidosTienda() {
        int idTienda = 101;
        List<Pedido> pedidos = List.of(new Pedido());

        when(pedidoRepository.findByIdTienda(idTienda)).thenReturn(pedidos);

        List<Pedido> resultado = pedidoService.listarPedidosTienda(idTienda);

        assertEquals(1, resultado.size());
    }

    @Test
    void testConsultarHistorial() {
        UUID idPedido = UUID.randomUUID();
        List<HistorialPedido> historial = List.of(new HistorialPedido());

        when(historialPedidoRepository.findByIdPedido(idPedido)).thenReturn(historial);

        List<HistorialPedido> resultado = pedidoService.consultarHistorial(idPedido);

        assertFalse(resultado.isEmpty());
    }

    @Test
    void testAgregarDetalle() {
        UUID idPedido = UUID.randomUUID();
        Pedido pedido = new Pedido();
        pedido.setIdPedido(idPedido);

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedido));

        PedidoDetalle detalleGuardado = new PedidoDetalle();
        detalleGuardado.setProducto("Eco Bottle");
        detalleGuardado.setCantidad(2);
        detalleGuardado.setPrecioUnitario(BigDecimal.valueOf(9.99));

        when(pedidoDetalleRepository.save(any(PedidoDetalle.class))).thenReturn(detalleGuardado);

        PedidoDetalle resultado = pedidoService.agregarDetalle(idPedido, "Eco Bottle", 2, BigDecimal.valueOf(9.99));

        assertNotNull(resultado);
        assertEquals("Eco Bottle", resultado.getProducto());
    }

    @Test
    void testObtenerDetalles() {
        UUID idPedido = UUID.randomUUID();
        List<PedidoDetalle> detalles = List.of(new PedidoDetalle());

        when(pedidoDetalleRepository.findByPedidoIdPedido(idPedido)).thenReturn(detalles);

        List<PedidoDetalle> resultado = pedidoService.obtenerDetalles(idPedido);

        assertFalse(resultado.isEmpty());
    }
}