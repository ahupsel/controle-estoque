package br.com.hupsel.controleestoque.api.pedido.dto;

import br.com.hupsel.controleestoque.dominio.pedido.PedidoItem;

import java.math.BigDecimal;

public record PedidoItemResponse(
        Long produtoId,
        Integer quantidade,
        BigDecimal precoUnitario
) {

    public static PedidoItemResponse from(PedidoItem item) {
        return new PedidoItemResponse(
                item.getProdutoId(),
                item.getQuantidade(),
                item.getPrecoUnitario()
        );
    }
}
