package br.com.abrantes.GerenciamentoCampo.dto.response;

import br.com.abrantes.GerenciamentoCampo.entity.ListaDeEsperaEntity;
import br.com.abrantes.GerenciamentoCampo.enums.StatusListaEspera;

import java.time.LocalDateTime;

public record ListaEsperaDto (
        Long id,

        Long usuarioId,

        Long campoId,

        LocalDateTime horaInicio,

        LocalDateTime horaFim,

        LocalDateTime criadoEm,

        StatusListaEspera status
){
    public ListaEsperaDto(ListaDeEsperaEntity listaDeEsperaEntity){
        this(
                listaDeEsperaEntity.getId(),
                listaDeEsperaEntity.getUsuario().getId(),
                listaDeEsperaEntity.getCampo().getId(),
                listaDeEsperaEntity.getHoraInicio(),
                listaDeEsperaEntity.getHoraFim(),
                listaDeEsperaEntity.getCriadoEm(),
                listaDeEsperaEntity.getStatus()
        );
    }
}
