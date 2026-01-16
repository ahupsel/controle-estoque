create table item_pedido (
    id bigserial primary key,
    pedido_id bigint not null,
    produto_id bigint not null,
    quantidade integer not null,
    preco_unitario numeric(10,2) not null,

    constraint fk_item_pedido
        foreign key (pedido_id)
        references pedido(id),

    constraint fk_item_produto
        foreign key (produto_id)
        references produto(id)
);