package com.micro7.micro7g3.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro7.micro7g3.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    List<Pedido> findByIdUsuario(UUID idUsuario);

    List<Pedido> findByIdTienda(UUID idTienda);
}
