package br.com.abrantes.GerenciamentoCampo.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EntrarListaEsperaDto(
        @NotNull
        Long usuarioId,

        @NotNull
        Long campoId,

        @NotNull
        LocalDateTime horaInicio,

        @NotNull
        LocalDateTime horaFim
) {
}
