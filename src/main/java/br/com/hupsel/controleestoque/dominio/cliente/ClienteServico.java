package br.com.hupsel.controleestoque.dominio.cliente;

import br.com.hupsel.controleestoque.api.cliente.dto.ClienteAtualizarRequest;
import br.com.hupsel.controleestoque.api.cliente.dto.ClienteCriarRequest;
import br.com.hupsel.controleestoque.api.cliente.dto.ClienteResponse;
import br.com.hupsel.controleestoque.integracao.viacep.ViaCepCliente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServico {

    private final ClienteRepositorio repositorio;
    private final ViaCepCliente viaCepCliente;

    public ClienteServico(ClienteRepositorio repositorio,  ViaCepCliente viaCepCliente) {
        this.repositorio = repositorio;
        this.viaCepCliente = viaCepCliente;
    }

    @Transactional
    public ClienteResponse criar(ClienteCriarRequest req) {

        var cliente = new Cliente(req.nome(), req.documento(), req.email());

        var endereco = viaCepCliente.buscarPorCep(req.cep());

        String cepLimpo = endereco.cep().replace("-", "");
        cliente.setCep(cepLimpo);
        cliente.setLogradouro(endereco.logradouro());
        cliente.setBairro(endereco.bairro());
        cliente.setCidade(endereco.localidade());
        cliente.setUf(endereco.uf());


        cliente = repositorio.save(cliente);

        return toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        var cliente = repositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        return toResponse(cliente);
    }

    @Transactional
    public ClienteResponse atualizar(Long id, ClienteAtualizarRequest req) {
        var cliente = repositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        cliente.setNome(req.nome());
        cliente.setEmail(req.email());

        cliente = repositorio.save(cliente);
        return toResponse(cliente);
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getDocumento(),
                cliente.getEmail()
        );
    }
}
