package br.com.hupsel.controleestoque.api.produto.dto;

import br.com.hupsel.controleestoque.dominio.produto.StatusProduto;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoAtualizarRequest(

        @NotBlank
        @Size(max = 255)
        String nome,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal preco,

        @NotNull
        @Min(0)
        Integer estoque,

        @NotNull
        StatusProduto status
) {}
