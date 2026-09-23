package br.com.abrantes.GerenciamentoCampo.repository;

import br.com.abrantes.GerenciamentoCampo.entity.CampoEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CampoRepository extends JpaRepository<CampoEntity, Long> {

boolean existsByNomeDoCampo(String nome);

@NativeQuery(
    value = """
    SELECT c.id as id,
           c.nome_do_campo as nomeDoCampo,
           c.status as status
    FROM campos c
""",
        countQuery = """
        SELECT COUNT(*)
        FROM campos c
"""
)
Page<CamposProjection> getAllCamposPage(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CampoEntity c WHERE c.id = :id")
    Optional<CampoEntity> findByIdForUpdate(@Param("id") Long id);
}
