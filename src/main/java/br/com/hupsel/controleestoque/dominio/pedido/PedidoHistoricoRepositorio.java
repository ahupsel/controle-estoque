package br.com.hupsel.controleestoque.dominio.pedido;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoHistoricoRepositorio extends JpaRepository<PedidoHistorico, Long> {
    List<PedidoHistorico> findByPedidoIdOrderByIdAsc(Long pedidoId);
}
