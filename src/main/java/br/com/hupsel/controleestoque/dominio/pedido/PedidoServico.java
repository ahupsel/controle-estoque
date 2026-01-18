package br.com.hupsel.controleestoque.dominio.pedido;

import br.com.hupsel.controleestoque.api.pedido.dto.PedidoCriarRequest;
import br.com.hupsel.controleestoque.api.pedido.dto.PedidoCriarResponse;
import br.com.hupsel.controleestoque.api.pedido.dto.PedidoResponse;
import br.com.hupsel.controleestoque.dominio.cliente.ClienteRepositorio;
import br.com.hupsel.controleestoque.dominio.produto.Produto;
import br.com.hupsel.controleestoque.dominio.produto.ProdutoRepositorio;
import br.com.hupsel.controleestoque.mensageria.pedido.PedidoCriadoEvento;
import br.com.hupsel.controleestoque.mensageria.pedido.PublicadorPedidoCriado;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoServico {

    public static final String TOPICO_PEDIDO_CRIADO = "pedido-criado";

    private final PedidoRepositorio pedidoRepositorio;
    private final ClienteRepositorio clienteRepositorio;
    private final ProdutoRepositorio produtoRepositorio;
    private final PedidoItemRepositorio pedidoItemRepositorio;
    private final PedidoHistoricoRepositorio historicoRepositorio;
    private final KafkaTemplate<String, PedidoCriadoEvento> kafkaTemplate;
    private final PublicadorPedidoCriado publicador;


    public PedidoServico(
            PedidoRepositorio pedidoRepositorio, ClienteRepositorio clienteRepositorio, ProdutoRepositorio produtoRepositorio,
            PedidoItemRepositorio pedidoItemRepositorio,
            PedidoHistoricoRepositorio historicoRepositorio,
            KafkaTemplate<String, PedidoCriadoEvento> kafkaTemplate,
            PublicadorPedidoCriado publicador
    ) {
        this.pedidoRepositorio = pedidoRepositorio;
        this.clienteRepositorio = clienteRepositorio;
        this.produtoRepositorio = produtoRepositorio;
        this.pedidoItemRepositorio = pedidoItemRepositorio;
        this.historicoRepositorio = historicoRepositorio;
        this.kafkaTemplate = kafkaTemplate;
        this.publicador = publicador;
    }

    @Transactional
    public PedidoCriarResponse criar(PedidoCriarRequest req) {
        var pedido = new Pedido(req.clienteId(), StatusPedido.CRIADO);
        pedido = pedidoRepositorio.save(pedido);

        for (var item : req.itens()) {
            var pedidoItem = new PedidoItem(
                    pedido.getId(),
                    item.produtoId(),
                    item.quantidade(),
                    java.math.BigDecimal.ZERO
            );
            pedidoItemRepositorio.save(pedidoItem);
        }

        historicoRepositorio.save(new PedidoHistorico(pedido.getId(), StatusPedido.CRIADO, "Pedido criado"));

        kafkaTemplate.send(TOPICO_PEDIDO_CRIADO, new PedidoCriadoEvento(pedido.getId()));
        //publicador.publicar(pedido.getId());


        return new PedidoCriarResponse(pedido.getId(), pedido.getStatus());
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        var pedido = pedidoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        var itens = pedidoItemRepositorio.findByPedidoId(pedido.getId());

        return PedidoResponse.from(pedido, itens);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return pedidoRepositorio.findAll().stream()
                .map(pedido -> {
                    var itens = pedidoItemRepositorio.findByPedidoId(pedido.getId());
                    return PedidoResponse.from(pedido, itens);
                })
                .toList();
    }

    @Transactional
    public void processar(Long pedidoId) {

        var pedido = pedidoRepositorio.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        // 🔒 proteção contra reprocessamento
        if (pedido.getStatus() != StatusPedido.CRIADO) {
            return;
        }

        pedido.atualizarStatus(StatusPedido.PROCESSANDO);
        pedidoRepositorio.save(pedido);

        historicoRepositorio.save(
                new PedidoHistorico(pedidoId, StatusPedido.PROCESSANDO, "Iniciando processamento")
        );

        boolean estoqueReservado = reservarEstoque(pedidoId);

        if (estoqueReservado) {
            pedido.atualizarStatus(StatusPedido.APROVADO);
            historicoRepositorio.save(
                    new PedidoHistorico(pedidoId, StatusPedido.APROVADO, "Pedido aprovado")
            );
        } else {
            pedido.atualizarStatus(StatusPedido.REJEITADO);
            historicoRepositorio.save(
                    new PedidoHistorico(pedidoId, StatusPedido.REJEITADO, "Estoque insuficiente")
            );
        }

        pedidoRepositorio.save(pedido);
    }



    @Transactional
    public boolean reservarEstoque(Long pedidoId) {

        var itens = pedidoItemRepositorio.findByPedidoId(pedidoId);

        for (var item : itens) {

            var produto = produtoRepositorio.findById(item.getProdutoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

            if (produto.getEstoque() < item.getQuantidade()) {
                return false;
            }
        }

        // Se chegou aqui, tem estoque para todos
        for (var item : itens) {

            var produto = produtoRepositorio.findById(item.getProdutoId()).get();
            produto.debitarEstoque(item.getQuantidade());

            produtoRepositorio.save(produto);
        }

        return true;
    }






}
