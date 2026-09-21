package br.com.abrantes.GerenciamentoCampo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegistroDto(
        @NotBlank
        String nome,

        @NotBlank
        String email,

        @NotBlank
        String senha
) {
}
