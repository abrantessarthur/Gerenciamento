package br.com.abrantes.GerenciamentoCampo.controller;

import br.com.abrantes.GerenciamentoCampo.dto.UserResponseDto;
import br.com.abrantes.GerenciamentoCampo.repository.UsuariosProjection;
import br.com.abrantes.GerenciamentoCampo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/page/{page}/size/{size}")
    public Page<UsuariosProjection> getAllCamposPageable(@PathVariable Integer page, @PathVariable Integer size){
        return usuarioService.getAllUsuariosPageable(page, size);
    }
}
