package br.com.abrantes.GerenciamentoCampo.dto;

import br.com.abrantes.GerenciamentoCampo.entity.ReservaEntity;
import br.com.abrantes.GerenciamentoCampo.enums.StatusReserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservaDto(
        Long id,
        Long usuarioId,
        Long campoId,
        LocalDateTime horaInicio,
        LocalDateTime horaFim,
        BigDecimal valor,
        StatusReserva status
) {
    public ReservaDto(ReservaEntity reservaEntity){
        this(
                reservaEntity.getId(),
                reservaEntity.getUsuario().getId(),
                reservaEntity.getCampo().getId(),
                reservaEntity.getHoraInicio(),
                reservaEntity.getHoraFim(),
                reservaEntity.getValor(),
                reservaEntity.getStatus()
        );
    }

}
