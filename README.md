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

2. Fluxo Geral do Pedido

O fluxo de criação e processamento de um pedido ocorre em duas etapas principais:

Criação do Pedido (Síncrona)
Processamento do Pedido (Assíncrona via Kafka)

Objetivo:

Registrar o pedido no sistema e publicar um evento indicando que ele está pronto para processamento.

Fluxo

O cliente envia uma requisição HTTP (POST /pedidos)
A API valida os dados recebidos
O pedido é persistido no banco com status inicial CRIADO
Um evento PedidoCriadoEvento é publicado no Kafka

Componentes envolvidos:

PedidoController
PedidoServico
PedidoRepositorio
PedidoCriadoPublicador (Interface - feita pra desacoplar o chamada do Kafka em um possivel cenario de imcompatibilidade) 

Pré-requisitos:
Docker
Docker Compose
Porta 8080, 5433, 6379 e 9092 livres (Postgres na porta 5433, pois ja tinha o mesmo instalado localmente)

Verifique:
docker --version
docker compose version

Na raiz do projeto, execute:
docker compose up -d --build (Isso irá subir automaticamente: PostgreSQL, Redis, Zookeeper, Kafka, Aplicação Spring Boot (retail_app))

Verificar se a aplicação subiu no Log:
docker logs -f retail_app

Health Check:
http://localhost:8080/actuator/health

Criar Cliente:
POST: POST http://localhost:8080/clients
Json: 
{
  "documento": "12345678900",
  "nome": "Cliente Teste",
  "email": "cliente@teste.com",
  "cep": "42710120"
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

> **Nota:** O pedido é criado com status CRIADO, processado de forma assíncrona via Kafka e atualizado automaticamente para PROCESSANDO → APROVADO / REJEITADO 
Para refeitar um pedido faça um pedido com uma quantidade maior que no estoque> 
    

Redis - Verificando cache:
docker exec -it retail_redis redis-cli
keys *

> **Nota:** A partir do segundo GET no produto o Redis criar o cache>


Para a aplicação: docker compose down
Remover volumes (dados): docker compose down -v


> **Nota:** Memory Leak - Objetivo>

Demonstrar a identificação, análise e correção de um cenário de vazamento de memória (memory leak) em uma aplicação Java com Spring Boot, mantendo a aplicação funcional após a correção.

Cenário de Vazamento - Foi criado um cenário intencional e controlado de memory leak, baseado em:

Uso de referência estática
Armazenamento contínuo de objetos em coleção sem política de liberação
Ausência de controle de ciclo de vida dos objetos

Esse padrão representa um problema comum em aplicações Java, especialmente em caches manuais, singletons e listeners mal gerenciados.

O vazamento pode ser identificado por:
Crescimento contínuo do uso de heap
Objetos não coletados pelo Garbage Collector
Retenção de instâncias via referências estáticas

Ferramentas recomendadas:
VisualVM
JConsole
Heap dump para análise de retenção de objetos
Correção

A correção foi realizada através de:
Remoção de referências estáticas desnecessárias
Garantia de liberação adequada dos objetos
Manutenção do controle de ciclo de vida das instâncias

Após a correção, o Garbage Collector passa a atuar corretamente e o consumo de memória se estabiliza.

Considerações Arquiteturais:
O cenário de memory leak foi mantido isolado do domínio de negócio
Nenhuma funcionalidade existente foi impactada
A aplicação permanece estável e funcional após a correção
