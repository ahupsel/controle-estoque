package br.com.hupsel.controleestoque.api.pedido.dto;

import br.com.hupsel.controleestoque.dominio.pedido.StatusPedido;

public record PedidoCriarResponse(
        Long pedidoId,
        StatusPedido status
) {}
