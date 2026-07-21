package br.com.abrantes.GerenciamentoCampo.repository;

import br.com.abrantes.GerenciamentoCampo.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
        Optional<UsuarioEntity> findByEmail(String email);
        Optional<UsuarioEntity> findByToken(String token);
        Optional<UsuarioEntity> findById(Long id);

    @NativeQuery
            (value = """
            SELECT u.id as id,
                   u.nome as nome,
                   u.email as email
            FROM usuarios u
            
""",
                    countQuery = """
                    SELECT COUNT(*)
                    FROM usuarios u
"""
    )
    Page<UsuariosProjection> gettUsuariosPage(Pageable pageable);


    UsuarioEntity findByEmailIgnoreCase(String email);
}
