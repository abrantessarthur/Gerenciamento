package br.com.abrantes.GerenciamentoCampo.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CriarReservaDto(

        @NotNull
        Long usuarioId,

        @NotNull
        Long campoId,

        @NotNull
        @Future
        LocalDateTime horaInicio,

        @NotNull
        @Future
        LocalDateTime horaFim

) {
}