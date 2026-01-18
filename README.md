Tecnologias Utilizadas

Java 17
Spring Boot
Spring Data JPA
Spring Kafka
Spring Cache + Redis
PostgreSQL
Apache Kafka + Zookeeper
Docker e Docker Compose
Flyway (migrations)




Explicação Arquitetural – Módulo de Criação e Processamento de Pedidos

1. Visão Geral da Arquitetura

O sistema foi projetado seguindo uma arquitetura em camadas, com separação clara entre:
Camada de API (Controller / DTOs)
Camada de Domínio (Regras de negócio)
Camada de Infraestrutura (Banco de dados, Cache e Mensageria)
Além disso, foi adotado o padrão de arquitetura orientada a eventos, utilizando Apache Kafka para desacoplar a criação do pedido do seu processamento.
Essa abordagem permite maior escalabilidade, desacoplamento e clareza no fluxo de negócio.

Pré-requisitos:
Docker
Docker Compose
Porta 8080, 5433, 6379 e 9092 livres (Postgres na porta 5433, pois ja tinha o mesmo instalado localmente)

Verifique:
docker --version
docker compose version

Na raiz do projeto, execute:
docker compose up -d --build (Isso irá subir automaticamente: PostgreSQL, Redis, Zookeeper, Kafka, Aplicação Spring Boot (retail_app))

Health Check:
http://localhost:8080/actuator/health

Criar Cliente:
POST: POST http://localhost:8080/clients
Json: 
{
  "documento": "12345678900",
  "nome": "Cliente Teste",
  "email": "cliente@teste.com",
  "cep": "01001000"
}

Criar Produto:
POST: http://localhost:8080/products
Json:
{
  "codigo": "PROD-001",
  "nome": "Produto Teste",
  "preco": 10.50,
  "estoque": 5,
  "status": "ATIVO"
}

Criar Pedido (O pedido é criado com status CRIADO, processado de forma assíncrona via Kafka e atualizado automaticamente para PROCESSANDO → APROVADO / REJEITADO.):
POST: http://localhost:8080/orders
Json:
{
  "clienteId": 1,
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2
    }
  ]
}

Redis - Verificando cache:
docker exec -it retail_redis redis-cli
keys *


Para a aplicação: docker compose down
Remover volumes (dados): docker compose down -v




