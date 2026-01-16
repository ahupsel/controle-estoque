package br.com.hupsel.controleestoque.api.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteAtualizarRequest(
        @NotBlank String nome,
        @NotBlank @Email String email
) {}
