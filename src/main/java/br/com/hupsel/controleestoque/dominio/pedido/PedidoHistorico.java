package br.com.hupsel.controleestoque.dominio.pedido;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "pedido_historico")
public class PedidoHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusPedido status;

    @Column(name = "mensagem", length = 500)
    private String mensagem;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @PrePersist
    void prePersist() {
        this.criadoEm = Instant.now();
    }

    public PedidoHistorico() {}

    public PedidoHistorico(Long pedidoId, StatusPedido status, String mensagem) {
        this.pedidoId = pedidoId;
        this.status = status;
        this.mensagem = mensagem;
    }

    public Long getId() { return id; }
}
