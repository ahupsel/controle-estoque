create table cliente (
  id bigserial primary key,
  nome varchar(150) not null,
  documento varchar(11) not null unique,
  email varchar(150) not null unique,
  telefone varchar(20),
  cep varchar(8),
  logradouro varchar(150),
  numero varchar(20),
  complemento varchar(100),
  bairro varchar(100),
  cidade varchar(100),
  uf varchar(2)
);