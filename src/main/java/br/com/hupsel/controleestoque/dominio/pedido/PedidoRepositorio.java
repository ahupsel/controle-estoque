package br.com.hupsel.controleestoque.dominio.pedido;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {}
