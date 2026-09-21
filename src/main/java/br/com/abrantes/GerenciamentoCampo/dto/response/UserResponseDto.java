package br.com.abrantes.GerenciamentoCampo.dto.response;

import br.com.abrantes.GerenciamentoCampo.entity.UsuarioEntity;

public record UserResponseDto(
        Long id,
        String nome,
        String email
) {
    public UserResponseDto(UsuarioEntity usuario){
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}
