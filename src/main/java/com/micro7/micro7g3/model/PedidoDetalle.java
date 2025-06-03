package com.micro7.micro7g3.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedido_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDetalle {

    @Id
    @GeneratedValue
    private UUID idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonBackReference
    private Pedido pedido;

    private String producto;

    private int cantidad;

    private BigDecimal precioUnitario;
}
