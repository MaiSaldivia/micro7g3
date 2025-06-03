package com.micro7.micro7g3.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro7.micro7g3.model.PedidoDetalle;

public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, UUID> {
    List<PedidoDetalle> findByPedidoIdPedido(UUID idPedido);
}
