package br.com.hupsel.controleestoque.api.cliente;

import br.com.hupsel.controleestoque.api.cliente.dto.ClienteAtualizarRequest;
import br.com.hupsel.controleestoque.api.cliente.dto.ClienteCriarRequest;
import br.com.hupsel.controleestoque.api.cliente.dto.ClienteResponse;
import br.com.hupsel.controleestoque.dominio.cliente.ClienteServico;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClienteController {

    private final ClienteServico servico;

    public ClienteController(ClienteServico servico) {
        this.servico = servico;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criar(@Valid @RequestBody ClienteCriarRequest req) {
        return servico.criar(req);
    }

    @GetMapping("/{id}")
    public ClienteResponse buscarPorId(@PathVariable Long id) {
        return servico.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(@PathVariable Long id, @Valid @RequestBody ClienteAtualizarRequest req) {
        return servico.atualizar(id, req);
    }
}
