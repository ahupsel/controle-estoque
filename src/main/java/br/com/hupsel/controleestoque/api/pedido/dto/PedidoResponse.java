package br.com.hupsel.controleestoque.api.pedido.dto;

import br.com.hupsel.controleestoque.dominio.pedido.Pedido;
import br.com.hupsel.controleestoque.dominio.pedido.PedidoItem;
import br.com.hupsel.controleestoque.dominio.pedido.StatusPedido;

import java.util.List;

public record PedidoResponse(
        Long id,
        Long clienteId,
        StatusPedido status,
        java.time.Instant dataCriacao,
        List<PedidoItemResponse> itens
) {

    public static PedidoResponse from(Pedido pedido, List<PedidoItem> itens) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getClienteId(),
                pedido.getStatus(),
                pedido.getDataCriacao(),
                itens.stream().map(PedidoItemResponse::from).toList()
        );
    }
}
