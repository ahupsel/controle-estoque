package br.com.hupsel.controleestoque.dominio.produto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {

    Optional<Produto> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
