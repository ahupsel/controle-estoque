package br.com.hupsel.controleestoque.dominio.produto;

import br.com.hupsel.controleestoque.api.produto.dto.ProdutoAtualizarRequest;
import br.com.hupsel.controleestoque.api.produto.dto.ProdutoCriarRequest;
import br.com.hupsel.controleestoque.api.produto.dto.ProdutoResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoServico {

    public static final String CACHE_POR_ID = "produtoPorId";
    public static final String CACHE_POR_CODIGO = "produtoPorCodigo";

    private final ProdutoRepositorio repositorio;

    public ProdutoServico(ProdutoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional
    public ProdutoResponse criar(ProdutoCriarRequest req) {
        if (repositorio.existsByCodigo(req.codigo())) {
            throw new IllegalArgumentException("Código de produto já existe: " + req.codigo());
        }

        var produto = new Produto(
                req.codigo(),
                req.nome(),
                req.preco(),
                req.estoque(),
                req.status()
        );

        var salvo = repositorio.save(produto);
        return ProdutoResponse.de(salvo);
    }

    @Cacheable(cacheNames = CACHE_POR_ID, key = "#id")
    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        var produto = repositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: id=" + id));

        return ProdutoResponse.de(produto);
    }

    @Cacheable(cacheNames = CACHE_POR_CODIGO, key = "#codigo")
    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorCodigo(String codigo) {
        var produto = repositorio.findByCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: codigo=" + codigo));

        return ProdutoResponse.de(produto);
    }

    @CacheEvict(cacheNames = {CACHE_POR_ID, CACHE_POR_CODIGO}, allEntries = true)
    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoAtualizarRequest req) {
        var produto = repositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: id=" + id));

        produto.setNome(req.nome());
        produto.setPreco(req.preco());
        produto.setEstoque(req.estoque());
        produto.setStatus(req.status());

        var salvo = repositorio.save(produto);
        return ProdutoResponse.de(salvo);
    }
}
