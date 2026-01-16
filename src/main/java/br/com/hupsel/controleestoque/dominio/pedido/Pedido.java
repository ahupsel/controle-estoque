package br.com.hupsel.controleestoque.dominio.pedido;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusPedido status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;


    @PrePersist
    void prePersist() {
        var agora = Instant.now();
        this.dataCriacao = agora;
    }

    public Pedido() {}

    public Pedido(Long clienteId, StatusPedido status) {
        this.clienteId = clienteId;
        this.status = status;
    }

    public Long getId() { return id; }
    public Long getClienteId() { return clienteId; }
    public StatusPedido getStatus() { return status; }
    public Instant getDataCriacao() { return dataCriacao; }

    public void setStatus(StatusPedido status) { this.status = status; }

    public void atualizarStatus(StatusPedido novoStatus) {
        this.status = novoStatus;
    }

}
