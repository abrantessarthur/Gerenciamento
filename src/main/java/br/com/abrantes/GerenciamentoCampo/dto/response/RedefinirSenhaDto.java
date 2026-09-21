package br.com.abrantes.GerenciamentoCampo.dto.response;

import jakarta.validation.constraints.NotBlank;

public record RedefinirSenhaDto(
        @NotBlank
        String token,

        @NotBlank
        String novaSenha
) {}
