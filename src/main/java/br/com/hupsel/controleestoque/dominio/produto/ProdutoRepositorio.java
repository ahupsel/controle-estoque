package br.com.hupsel.controleestoque.dominio.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {

    Optional<Produto> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    @Modifying
    @Query("""
        update Produto p
           set p.estoque = p.estoque - :quantidade
         where p.id = :produtoId
           and p.estoque >= :quantidade
    """)
    int debitarEstoque(Long produtoId, Integer quantidade);
}
