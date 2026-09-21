package br.com.abrantes.GerenciamentoCampo.dto.response;

import java.time.LocalDateTime;

public record HorarioDisponivelDto(
        LocalDateTime horaInicio,

        LocalDateTime horaFim
) {
}
