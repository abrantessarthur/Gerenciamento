package br.com.abrantes.GerenciamentoCampo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RedefinirSenhaDto(
        @NotBlank
        String token,

        @NotBlank
        String novaSenha
) {}
