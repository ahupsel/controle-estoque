package br.com.hupsel.controleestoque.api.cliente.dto;

public record ClienteResponse(
        Long id,
        String nome,
        String documento,
        String email
) {}
