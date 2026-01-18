package br.com.hupsel.controleestoque.dominio.produto;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "preco", nullable = false, precision = 15, scale = 2)
    private BigDecimal preco;

    @Column(name = "estoque", nullable = false)
    private Integer estoque;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusProduto status;

    @PrePersist
    void prePersist() {
        var agora = Instant.now();
    }

    public Produto() {}

    public Produto(String codigo, String nome, BigDecimal preco, Integer estoque, StatusProduto status) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }
    public Integer getEstoque() { return estoque; }
    public StatusProduto getStatus() { return status; }

    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }
    public void setStatus(StatusProduto status) { this.status = status; }

    public void debitarEstoque(Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida");
        }

        if (this.estoque < quantidade) {
            throw new IllegalStateException("Estoque insuficiente");
        }

        this.estoque -= quantidade;
    }
}
