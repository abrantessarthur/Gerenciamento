package br.com.abrantes.GerenciamentoCampo.repository;

import br.com.abrantes.GerenciamentoCampo.entity.ListaDeEsperaEntity;
import br.com.abrantes.GerenciamentoCampo.enums.StatusListaEspera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ListaDeEsperaRepository extends JpaRepository<ListaDeEsperaEntity, Long> {
    boolean existsByUsuario_IdAndCampo_IdAndHoraInicioAndHoraFimAndStatus(
            Long usuarioId,
            Long campoId,
            LocalDateTime horaInicio,
            LocalDateTime horaFim,
            StatusListaEspera status
    );

    Optional<ListaDeEsperaEntity> findFirstByCampo_IdAndHoraInicioAndHoraFimAndStatusOrderByCriadoEmAsc(
            Long campoId,
            LocalDateTime horaInicio,
            LocalDateTime horaFim,
            StatusListaEspera status
    );
}
