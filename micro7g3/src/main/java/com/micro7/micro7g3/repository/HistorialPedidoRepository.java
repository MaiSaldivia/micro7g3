package com.micro7.micro7g3.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro7.micro7g3.model.HistorialPedido;

public interface HistorialPedidoRepository extends JpaRepository<HistorialPedido, UUID> {

    List<HistorialPedido> findByIdPedido(UUID idPedido);
}
