create table pedido (
    id bigserial primary key,
    cliente_id bigint not null,
    data_criacao timestamp not null,
    status varchar(30) not null,
    constraint fk_pedido_cliente
        foreign key (cliente_id)
        references cliente(id)
);
