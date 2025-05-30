package com.micro7.micro7g3.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.micro7.micro7g3.model.EstadoPedido;
import com.micro7.micro7g3.model.HistorialPedido;
import com.micro7.micro7g3.model.Pedido;
import com.micro7.micro7g3.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/crear")
    public Pedido crearPedido(@RequestParam UUID idUsuario, @RequestParam UUID idTienda) {
        return pedidoService.crearPedido(idUsuario, idTienda);
    }

    @PutMapping("/cancelar/{idPedido}")
    public void cancelarPedido(@PathVariable UUID idPedido) {
        pedidoService.cancelarPedido(idPedido);
    }

    @PutMapping("/estado/{idPedido}")
    public void cambiarEstado(@PathVariable UUID idPedido, @RequestParam EstadoPedido nuevoEstado) {
        pedidoService.cambiarEstadoPedido(idPedido, nuevoEstado);
    }

    @GetMapping("/{idPedido}")
    public String consultarPedido(@PathVariable UUID idPedido) {
        Pedido pedido = pedidoService.consultarPedido(idPedido);
        System.out.println("Pedido encontrado: " + pedido);
        return pedido != null ? pedido.toString() : "No encontrado";
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Pedido> listarPorUsuario(@PathVariable UUID idUsuario) {
        return pedidoService.listarPedidosUsuario(idUsuario);
    }

    @GetMapping("/tienda/{idTienda}")
    public List<Pedido> listarPorTienda(@PathVariable UUID idTienda) {
        return pedidoService.listarPedidosTienda(idTienda);
    }

    @GetMapping("/historial/{idPedido}")
    public List<HistorialPedido> consultarHistorial(@PathVariable UUID idPedido) {
        return pedidoService.consultarHistorial(idPedido);
    }
}
