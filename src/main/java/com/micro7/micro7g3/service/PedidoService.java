package com.micro7.micro7g3.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.micro7.micro7g3.model.EstadoPedido;
import com.micro7.micro7g3.model.HistorialPedido;
import com.micro7.micro7g3.model.Pedido;
import com.micro7.micro7g3.model.PedidoDetalle;
import com.micro7.micro7g3.repository.HistorialPedidoRepository;
import com.micro7.micro7g3.repository.PedidoDetalleRepository;
import com.micro7.micro7g3.repository.PedidoRepository;


@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final HistorialPedidoRepository historialPedidoRepository;
    private final PedidoDetalleRepository pedidoDetalleRepository;

    public PedidoService(PedidoRepository pedidoRepository, HistorialPedidoRepository historialPedidoRepository, PedidoDetalleRepository pedidoDetalleRepository) {
        this.pedidoRepository = pedidoRepository;
        this.historialPedidoRepository = historialPedidoRepository;
        this.pedidoDetalleRepository = pedidoDetalleRepository;
    }

    public Pedido crearPedido(UUID idUsuario, UUID idTienda) {
        Pedido pedido = new Pedido();
        pedido.setIdUsuario(idUsuario);
        pedido.setIdTienda(idTienda);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        return pedidoRepository.save(pedido);
    }

    public boolean cancelarPedido(UUID idPedido) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);
        if (pedidoOpt.isEmpty()) return false;

        Pedido pedido = pedidoOpt.get();
        EstadoPedido anterior = pedido.getEstado();
        pedido.setEstado(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);
        registrarCambioEstado(idPedido, anterior, EstadoPedido.CANCELADO);
        return true;
    }

    public boolean cambiarEstadoPedido(UUID idPedido, EstadoPedido nuevoEstado) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);
        if (pedidoOpt.isEmpty()) return false;

        Pedido pedido = pedidoOpt.get();
        EstadoPedido anterior = pedido.getEstado();
        pedido.setEstado(nuevoEstado);
        pedidoRepository.save(pedido);
        registrarCambioEstado(idPedido, anterior, nuevoEstado);
        return true;
    }

    public Pedido consultarPedido(UUID idPedido) {
        return pedidoRepository.findById(idPedido).orElse(null);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPedidosUsuario(UUID idUsuario) {
        return pedidoRepository.findByIdUsuario(idUsuario);
    }

    public List<Pedido> listarPedidosTienda(UUID idTienda) {
        return pedidoRepository.findByIdTienda(idTienda);
    }

    public List<HistorialPedido> consultarHistorial(UUID idPedido) {
        return historialPedidoRepository.findByIdPedido(idPedido);
    }

    private void registrarCambioEstado(UUID idPedido, EstadoPedido anterior, EstadoPedido nuevo) {
        HistorialPedido historial = new HistorialPedido();
        historial.setIdPedido(idPedido);
        historial.setFechaCambio(LocalDateTime.now());
        historial.setEstadoAnterior(anterior);
        historial.setEstadoNuevo(nuevo);
        historialPedidoRepository.save(historial);
    }

    public PedidoDetalle agregarDetalle(UUID idPedido, String producto, int cantidad, BigDecimal precioUnitario) {
        Pedido pedido = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        PedidoDetalle detalle = new PedidoDetalle();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);

        return pedidoDetalleRepository.save(detalle);
    }
    
    public List<PedidoDetalle> obtenerDetalles(UUID idPedido) {
        return pedidoDetalleRepository.findByPedidoIdPedido(idPedido);
    }

}
