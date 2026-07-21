package br.com.abrantes.GerenciamentoCampo.repository;

import br.com.abrantes.GerenciamentoCampo.dto.CampoDto;
import br.com.abrantes.GerenciamentoCampo.entity.CampoEntity;
import br.com.abrantes.GerenciamentoCampo.entity.ReservaEntity;
import br.com.abrantes.GerenciamentoCampo.enums.StatusReserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    List<ReservaEntity> findByCampo_Id(Long campoId);

    List<ReservaEntity> findByUsuario_Id(Long usuarioId);

    List<ReservaEntity> findByHoraInicioBetween(
            LocalDateTime inicio,
            LocalDateTime fim
    );

    List<ReservaEntity> findByCampo_IdAndHoraInicioBetween(
            Long campoId,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    List<ReservaEntity> findByUsuario_IdAndHoraInicioBetween(
            Long usuarioId,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    @NativeQuery(
            value = """
            SELECT r.id as id,
                   r.usuario_id as usuarioId,
                   r.campo_id as campoId,
                   r.hora_inicio as horaInicio,
                   r.hora_fim as horaFim,
                   r.valor as valor,
                   r.status as status
            FROM reserva r
""",
            countQuery = """
            SELECT COUNT(*)
            FROM reserva r 
        """
    )
    Page<ReservasProjection> getAllReservasPageable(Pageable pageable);

    @Query("""
    SELECT COUNT(r) > 0 
    FROM ReservaEntity r 
    WHERE r.campo.id = :campoId 
      AND (:horaInicio < r.horaFim AND :horaFim > r.horaInicio)
      AND (:idReserva IS NULL OR r.id <> :idReserva)
""")
    boolean existsOverlappingReserva(
            @Param("campoId") Long campoId,
            @Param("horaInicio") LocalDateTime horaInicio,
            @Param("horaFim") LocalDateTime horaFim,
            @Param("idReserva") Long idReserva
    );

    @Query("SELECT r.campo " +
            "FROM ReservaEntity r " +
            "WHERE r.status = :status " +
            "GROUP BY r.campo " +
            "ORDER BY COUNT(r) DESC")
    List<CampoDto> findCampoMaisUsado(@Param("status") StatusReserva status, Pageable pageable);

    @Query("""
    SELECT r.campo
    FROM ReservaEntity r
    GROUP BY r.campo
    ORDER BY SUM(r.valor) DESC
""")
    List<CampoDto> findCampoMaisLucrativo(Pageable pageable);

    @Query("""
    SELECT r FROM ReservaEntity r
    WHERE r.campo.id = :campoId
      AND r.status IN (br.com.abrantes.GerenciamentoCampo.enums.StatusReserva.PENDENTE,
                        br.com.abrantes.GerenciamentoCampo.enums.StatusReserva.CONFIRMADA)
      AND r.horaInicio < :fimDoDia
      AND r.horaFim > :inicioDoDia
    ORDER BY r.horaInicio
""")
    List<ReservaEntity> findReservasAtivasDoCampoNoDia(
            @Param("campoId") Long campoId,
            @Param("inicioDoDia") LocalDateTime inicioDoDia,
            @Param("fimDoDia") LocalDateTime fimDoDia
    );

    @Query("""
    SELECT c FROM CampoEntity c
    WHERE c.status = br.com.abrantes.GerenciamentoCampo.enums.Status.DISPONIVEL
      AND c.id NOT IN (
          SELECT r.campo.id FROM ReservaEntity r
          WHERE r.status IN (br.com.abrantes.GerenciamentoCampo.enums.StatusReserva.PENDENTE,
                              br.com.abrantes.GerenciamentoCampo.enums.StatusReserva.CONFIRMADA)
            AND :horaInicio < r.horaFim AND :horaFim > r.horaInicio
      )
""")
    List<CampoEntity> findCamposDisponiveisNoHorario(
            @Param("horaInicio") LocalDateTime horaInicio,
            @Param("horaFim") LocalDateTime horaFim
    );
}