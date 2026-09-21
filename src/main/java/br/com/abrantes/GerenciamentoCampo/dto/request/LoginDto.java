package br.com.abrantes.GerenciamentoCampo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank
        String email,

        @NotBlank
        String senha
) {
}
