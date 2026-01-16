package br.com.hupsel.controleestoque.mensageria.pedido;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("docker")
public class KafkaPublicadorPedidoCriado implements PublicadorPedidoCriado {

    private final KafkaTemplate<String, PedidoCriadoEvento> kafkaTemplate;

    public KafkaPublicadorPedidoCriado(
            KafkaTemplate<String, PedidoCriadoEvento> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publicar(Long pedidoId) {
        kafkaTemplate.send(
                "pedido-criado",
                new PedidoCriadoEvento(pedidoId)
        );
    }
}
