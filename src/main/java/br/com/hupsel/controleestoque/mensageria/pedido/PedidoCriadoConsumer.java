package br.com.hupsel.controleestoque.mensageria.pedido;

import br.com.hupsel.controleestoque.dominio.pedido.PedidoServico;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Profile("docker")
public class PedidoCriadoConsumer {

    private final PedidoServico pedidoServico;

    public PedidoCriadoConsumer(PedidoServico pedidoServico) {
        this.pedidoServico = pedidoServico;
    }

    @KafkaListener(
            topics = "pedido-criado",
            groupId = "pedido-processador"

    )
    public void consumir(PedidoCriadoEvento evento) {
        pedidoServico.processar(evento.pedidoId());
    }
}
