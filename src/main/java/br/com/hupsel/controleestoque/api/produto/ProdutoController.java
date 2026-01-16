package br.com.hupsel.controleestoque.api.produto;

import br.com.hupsel.controleestoque.api.produto.dto.ProdutoAtualizarRequest;
import br.com.hupsel.controleestoque.api.produto.dto.ProdutoCriarRequest;
import br.com.hupsel.controleestoque.api.produto.dto.ProdutoResponse;
import br.com.hupsel.controleestoque.dominio.produto.ProdutoServico;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProdutoController {

    private final ProdutoServico servico;

    public ProdutoController(ProdutoServico servico) {
        this.servico = servico;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse criar(@Valid @RequestBody ProdutoCriarRequest req) {
        return servico.criar(req);
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable Long id) {
        return servico.buscarPorId(id);
    }

    @GetMapping("/code/{codigo}")
    public ProdutoResponse buscarPorCodigo(@PathVariable String codigo) {
        return servico.buscarPorCodigo(codigo);
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoAtualizarRequest req) {
        return servico.atualizar(id, req);
    }
}
