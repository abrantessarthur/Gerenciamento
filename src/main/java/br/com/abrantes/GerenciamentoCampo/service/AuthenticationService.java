package br.com.abrantes.GerenciamentoCampo.service;

import br.com.abrantes.GerenciamentoCampo.dto.RedefinirSenhaDto;
import br.com.abrantes.GerenciamentoCampo.entity.RolesEntity;
import br.com.abrantes.GerenciamentoCampo.entity.UsuarioEntity;
import br.com.abrantes.GerenciamentoCampo.enums.RoleTypeEnum;
import br.com.abrantes.GerenciamentoCampo.config.TokenProvider;
import br.com.abrantes.GerenciamentoCampo.dto.LoginDto;
import br.com.abrantes.GerenciamentoCampo.dto.RegistroDto;
import br.com.abrantes.GerenciamentoCampo.dto.TokenResponseDTO;
import br.com.abrantes.GerenciamentoCampo.exception.BadRequestException;
import br.com.abrantes.GerenciamentoCampo.repository.RolesRepository;
import br.com.abrantes.GerenciamentoCampo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UsuarioRepository usuarioRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Value("${jwt.expiration:900000}")
    private long expirationTime;

    public void register(RegistroDto registro) {
        usuarioRepository.findByEmail(registro.email())
                .ifPresent(u -> {
                    throw new BadRequestException("Usuário já cadastrado com esse email");
                });

        RolesEntity role = rolesRepository.findByNome(RoleTypeEnum.ROLE_USUARIO.name())
                .orElseGet(() -> rolesRepository.save(
                        RolesEntity.builder().nome(RoleTypeEnum.ROLE_USUARIO.name()).build()));

        UsuarioEntity novoUsuario = UsuarioEntity.builder()
                .email(registro.email())
                .nome(registro.nome())
                .senha(passwordEncoder.encode(registro.senha()))
                .roles(java.util.Set.of(role))
                .build();

        usuarioRepository.save(novoUsuario);
    }

    public TokenResponseDTO login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.senha()));
            String token = tokenProvider.generateToken(authentication);

            return new TokenResponseDTO(token, expirationTime);
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Credenciais inválidas");
        }
    }

    @Transactional
    public void redefinirSenha(RedefinirSenhaDto dto) {
        UsuarioEntity usuario = usuarioRepository.findByToken(dto.token())
                .orElseThrow(() -> new BadRequestException("Token inválido ou inexistente."));
        if (usuario.getExpiracaoToken() == null || usuario.getExpiracaoToken().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("O token de recuperação expirou. Solicite um novo.");
        }
        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuario.setToken(null);
        usuario.setExpiracaoToken(null);

        usuarioRepository.save(usuario);
    }
}
