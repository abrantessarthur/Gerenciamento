package br.com.abrantes.GerenciamentoCampo.repository;

import br.com.abrantes.GerenciamentoCampo.dto.CampoDto;
import br.com.abrantes.GerenciamentoCampo.entity.CampoEntity;
import br.com.abrantes.GerenciamentoCampo.entity.ReservaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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


}
