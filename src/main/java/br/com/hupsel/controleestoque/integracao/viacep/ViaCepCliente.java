package br.com.hupsel.controleestoque.integracao.viacep;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ViaCepCliente {

    private final RestTemplate restTemplate = new RestTemplate();

    public ViaCepResponse buscarPorCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        ViaCepResponse response = restTemplate.getForObject(url, ViaCepResponse.class);

        if (response == null || Boolean.TRUE.equals(response.erro())) {
            throw new IllegalArgumentException("CEP inválido: " + cep);
        }
        return response;
    }
}
