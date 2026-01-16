package br.com.hupsel.controleestoque.api.produto.dto;

import br.com.hupsel.controleestoque.dominio.produto.Produto;
import br.com.hupsel.controleestoque.dominio.produto.StatusProduto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String codigo,
        String nome,
        BigDecimal preco,
        Integer estoque,
        StatusProduto status
) {
    public static ProdutoResponse de(Produto p) {
        return new ProdutoResponse(
                p.getId(),
                p.getCodigo(),
                p.getNome(),
                p.getPreco(),
                p.getEstoque(),
                p.getStatus()
        );
    }
}
