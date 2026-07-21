package br.com.abrantes.GerenciamentoCampo.service;

import br.com.abrantes.GerenciamentoCampo.dto.UserResponseDto;
import br.com.abrantes.GerenciamentoCampo.entity.UsuarioEntity;
import br.com.abrantes.GerenciamentoCampo.exception.UsuarioNaoEncontradoException;
import br.com.abrantes.GerenciamentoCampo.repository.UsuarioRepository;
import br.com.abrantes.GerenciamentoCampo.repository.UsuariosProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;


    @Transactional(readOnly = true)
    public List<UserResponseDto> listarUsuarios() {
        if (usuarioRepository.count() == 0) {
            throw new UsuarioNaoEncontradoException("Nenhum usuario encontrado");
        }
        return usuarioRepository.findAll()
                .stream()
                .map(UserResponseDto::new)
                .toList()
                ;
    }

    @Transactional(readOnly = true)
    public UserResponseDto listarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(UserResponseDto::new)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException("Usuário inexistente"));
    }

    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontradoException("Usuário inexistente");
        }
        usuarioRepository.deleteById(id);
    }

    public Page<UsuariosProjection> getAllUsuariosPageable(Integer page, Integer size){
        return usuarioRepository.gettUsuariosPage(PageRequest.of(page, size));
    }

    public void enviarToken(String email){

        UsuarioEntity usuario = usuarioRepository.findByEmailIgnoreCase(email);

        String token = UUID.randomUUID().toString();
        usuario.setToken(token);
        usuario.setExpiracaoToken(LocalDateTime.now().plusMinutes(5));
        usuarioRepository.save(usuario);

        emailService.enviarEmailSenha(usuario);
    }


}
