package com.micro7.micro7g3.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.micro7.micro7g3.model.EstadoPedido;
import com.micro7.micro7g3.model.HistorialPedido;
import com.micro7.micro7g3.model.Pedido;
import com.micro7.micro7g3.model.PedidoDetalle;
import com.micro7.micro7g3.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/crear")
    public Pedido crearPedido(@RequestParam String idUsuario, @RequestParam int idTienda) {
        return pedidoService.crearPedido(idUsuario, idTienda);
    }

    @PutMapping("/cancelar/{idPedido}")
    public ResponseEntity<String> cancelarPedido(@PathVariable UUID idPedido) {
        boolean exito = pedidoService.cancelarPedido(idPedido);
        return exito
            ? ResponseEntity.ok("Pedido cancelado correctamente")
            : ResponseEntity.status(404).body("Pedido no encontrado");
    }

    @PutMapping("/estado/{idPedido}")
    public ResponseEntity<String> cambiarEstado(@PathVariable UUID idPedido, @RequestParam EstadoPedido nuevoEstado) {
        boolean exito = pedidoService.cambiarEstadoPedido(idPedido, nuevoEstado);
        return exito
            ? ResponseEntity.ok("Estado actualizado correctamente")
            : ResponseEntity.status(404).body("Pedido no encontrado");
    }

    @GetMapping("/{idPedido}")
    public String consultarPedido(@PathVariable UUID idPedido) {
        Pedido pedido = pedidoService.consultarPedido(idPedido);
        System.out.println("Pedido encontrado: " + pedido);
        return pedido != null ? pedido.toString() : "No encontrado";
    }

    @GetMapping
    public List<Pedido> listarTodosLosPedidos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Pedido> listarPorUsuario(@PathVariable String idUsuario) {
        return pedidoService.listarPedidosUsuario(idUsuario);
    }

    @GetMapping("/tienda/{idTienda}")
    public List<Pedido> listarPorTienda(@PathVariable int idTienda) {
        return pedidoService.listarPedidosTienda(idTienda);
    }

    @GetMapping("/historial/{idPedido}")
    public List<HistorialPedido> consultarHistorial(@PathVariable UUID idPedido) {
        return pedidoService.consultarHistorial(idPedido);
    }

    @PostMapping("/{idPedido}/agregar-linea")
    public ResponseEntity<PedidoDetalle> agregarDetalle(
        @PathVariable UUID idPedido,
        @RequestParam String producto,
        @RequestParam int cantidad,
        @RequestParam BigDecimal precioUnitario
    ) {
        PedidoDetalle detalle = pedidoService.agregarDetalle(idPedido, producto, cantidad, precioUnitario);
        return ResponseEntity.ok(detalle);
    }

    @GetMapping("/{idPedido}/detalles")
    public ResponseEntity<List<PedidoDetalle>> obtenerDetalles(@PathVariable UUID idPedido) {
        List<PedidoDetalle> detalles = pedidoService.obtenerDetalles(idPedido);
        return detalles.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(detalles);
    }
}