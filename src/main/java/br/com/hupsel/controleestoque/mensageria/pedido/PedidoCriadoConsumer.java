package br.com.hupsel.controleestoque.mensageria.pedido;

import br.com.hupsel.controleestoque.dominio.pedido.PedidoServico;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoCriadoConsumer {

    private final PedidoServico pedidoServico;

    public PedidoCriadoConsumer(PedidoServico pedidoServico) {
        this.pedidoServico = pedidoServico;
        System.out.println(">>> PedidoCriadoConsumer INSTANCIADO");
    }

    @KafkaListener(topics = "pedido-criado", groupId = "pedido-processador")
    public void consumir(PedidoCriadoEvento evento) {
        System.out.println(">>> EVENTO RECEBIDO: pedidoId=" + evento.pedidoId());
        pedidoServico.processar(evento.pedidoId());
    }
}
