package br.com.abrantes.GerenciamentoCampo.repository;

import br.com.abrantes.GerenciamentoCampo.entity.ListaDeEsperaEntity;
import br.com.abrantes.GerenciamentoCampo.enums.StatusListaEspera;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM ListaDeEsperaEntity l WHERE l.id = :id")
    Optional<ListaDeEsperaEntity> findByIdForUpdate(@Param("id") Long id);
}
