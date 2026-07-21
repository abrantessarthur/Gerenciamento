package br.com.abrantes.GerenciamentoCampo.dto;

import java.time.LocalDateTime;

public record HorarioDisponivelDto(
        LocalDateTime horaInicio,

        LocalDateTime horaFim
) {
}
