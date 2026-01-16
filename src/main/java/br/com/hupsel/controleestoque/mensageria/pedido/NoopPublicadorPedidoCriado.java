package br.com.hupsel.controleestoque.mensageria.pedido;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!docker")
public class NoopPublicadorPedidoCriado implements PublicadorPedidoCriado {

    @Override
    public void publicar(Long pedidoId) {
        // NÃO FAZ NADA
    }
}
