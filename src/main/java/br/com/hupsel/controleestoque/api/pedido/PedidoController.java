package br.com.hupsel.controleestoque.api.pedido;

import br.com.hupsel.controleestoque.api.pedido.dto.PedidoCriarRequest;
import br.com.hupsel.controleestoque.api.pedido.dto.PedidoCriarResponse;
import br.com.hupsel.controleestoque.api.pedido.dto.PedidoResponse;
import br.com.hupsel.controleestoque.dominio.pedido.PedidoServico;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class PedidoController {

    private final PedidoServico servico;

    public PedidoController(PedidoServico servico) {
        this.servico = servico;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoCriarResponse criar(@Valid @RequestBody PedidoCriarRequest req) {
        return servico.criar(req);
    }

    @GetMapping("/{id}")
    public PedidoResponse buscarPorId(@PathVariable Long id) {
        return servico.buscarPorId(id);
    }

    @GetMapping
    public List<PedidoResponse> listar() {
        return servico.listar();
    }


}
