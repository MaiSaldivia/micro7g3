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
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue
    private UUID idPedido;

    @Column(nullable = false)
    private UUID idUsuario;

    @Column(nullable = false)
    private UUID idTienda;

    private LocalDateTime fechaPedido;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private EstadoPedido estado;
}
