package br.com.hupsel.controleestoque.api.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteCriarRequest(
        @NotBlank String documento,
        @NotBlank String nome,
        @NotBlank String email
) {}
