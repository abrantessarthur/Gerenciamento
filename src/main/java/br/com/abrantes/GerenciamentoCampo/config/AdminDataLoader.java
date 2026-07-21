package br.com.abrantes.GerenciamentoCampo.config;

import br.com.abrantes.GerenciamentoCampo.entity.RolesEntity;
import br.com.abrantes.GerenciamentoCampo.entity.UsuarioEntity;
import br.com.abrantes.GerenciamentoCampo.enums.RoleTypeEnum;
import br.com.abrantes.GerenciamentoCampo.repository.RolesRepository;
import br.com.abrantes.GerenciamentoCampo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminDataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 1. Garante que as Roles existam no banco
        RolesEntity roleAdmin = rolesRepository.findByNome("ROLE_ADMIN")
                .orElseGet(() -> rolesRepository.save(
                        RolesEntity.builder().nome("ROLE_ADMIN").build()));

        rolesRepository.findByNome(RoleTypeEnum.ROLE_USUARIO.name())
                .orElseGet(() -> rolesRepository.save(
                        RolesEntity.builder().nome(RoleTypeEnum.ROLE_USUARIO.name()).build()));

        // 2. Cria o usuário Admin padrão se ele ainda não existir
        if (usuarioRepository.findByEmail("admin@email.com").isEmpty()) {
            UsuarioEntity admin = UsuarioEntity.builder()
                    .nome("Administrador")
                    .email("admin@email.com")
                    .senha(passwordEncoder.encode("admin123")) // Criptografa com BCrypt
                    .roles(Set.of(roleAdmin))
                    .build();

            usuarioRepository.save(admin);
            System.out.println("✅ ADMIN CRIADO AUTOMATICAMENTE: admin@email.com | senha: admin123");
        }
    }
}