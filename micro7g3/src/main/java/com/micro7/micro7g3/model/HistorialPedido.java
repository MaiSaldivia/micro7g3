package com.micro7.micro7g3.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "historial_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialPedido {

    @Id
    @GeneratedValue
    private UUID idHistorial;

    @Column(nullable = false)
    private UUID idPedido;

    private LocalDateTime fechaCambio;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private EstadoPedido estadoAnterior;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private EstadoPedido estadoNuevo;
}
