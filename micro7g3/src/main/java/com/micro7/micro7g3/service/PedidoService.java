package com.micro7.micro7g3.service;

import com.micro7.micro7g3.model.EstadoPedido;
import com.micro7.micro7g3.model.HistorialPedido;
import com.micro7.micro7g3.model.Pedido;
import com.micro7.micro7g3.repository.HistorialPedidoRepository;
import com.micro7.micro7g3.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final HistorialPedidoRepository historialPedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository, HistorialPedidoRepository historialPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.historialPedidoRepository = historialPedidoRepository;
    }

    public Pedido crearPedido(UUID idUsuario, UUID idTienda) {
        Pedido pedido = new Pedido();
        pedido.setIdUsuario(idUsuario);
        pedido.setIdTienda(idTienda);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        return pedidoRepository.save(pedido);
    }

    public void cancelarPedido(UUID idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElse(null);
        if (pedido != null) {
            EstadoPedido anterior = pedido.getEstado();
            pedido.setEstado(EstadoPedido.CANCELADO);
            pedidoRepository.save(pedido);
            registrarCambioEstado(idPedido, anterior, EstadoPedido.CANCELADO);
        }
    }

    public void cambiarEstadoPedido(UUID idPedido, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido).orElse(null);
        if (pedido != null) {
            EstadoPedido anterior = pedido.getEstado();
            pedido.setEstado(nuevoEstado);
            pedidoRepository.save(pedido);
            registrarCambioEstado(idPedido, anterior, nuevoEstado);
        }
    }

    public Pedido consultarPedido(UUID idPedido) {
        return pedidoRepository.findById(idPedido).orElse(null);
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
}
