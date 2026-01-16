create table pedido_historico (
    id bigserial primary key,
    pedido_id bigint not null,
    status varchar(50) not null,
    mensagem varchar(255),
    criado_em timestamp not null default now(),

    constraint fk_pedido_historico_pedido
        foreign key (pedido_id)
        references pedido(id)
);
