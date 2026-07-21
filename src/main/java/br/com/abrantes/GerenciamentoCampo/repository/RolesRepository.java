package br.com.abrantes.GerenciamentoCampo.repository;
import br.com.abrantes.GerenciamentoCampo.entity.RolesEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<RolesEntity, Long> {
    Optional<RolesEntity> findByNome(String role);
}
