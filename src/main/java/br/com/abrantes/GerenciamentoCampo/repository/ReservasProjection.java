package br.com.abrantes.GerenciamentoCampo.repository;

import br.com.abrantes.GerenciamentoCampo.enums.StatusReserva;

import java.time.LocalDateTime;

public interface ReservasProjection {
    Long getId();
    Long getIdUsuario();
    Long getIdCampo();
    LocalDateTime getHoraInicio();
    LocalDateTime getHoraFim();
    Double getValor();
    StatusReserva getStatusReserva();
}
