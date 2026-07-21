package br.com.abrantes.GerenciamentoCampo.config;

import br.com.abrantes.GerenciamentoCampo.dto.RedefinirSenhaDto;
import br.com.abrantes.GerenciamentoCampo.service.AuthenticationService;
import br.com.abrantes.GerenciamentoCampo.dto.LoginDto;
import br.com.abrantes.GerenciamentoCampo.dto.RegistroDto;
import br.com.abrantes.GerenciamentoCampo.dto.TokenResponseDTO;
import br.com.abrantes.GerenciamentoCampo.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<Void> registrar(@RequestBody @Valid RegistroDto registroDto) {
        authenticationService.register(registroDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginDto loginDto) {
        TokenResponseDTO token = authenticationService.login(loginDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<Void> solicitarRecuperacao(@RequestParam String email) {
        usuarioService.enviarToken(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(@RequestBody @Valid RedefinirSenhaDto dto) {
        authenticationService.redefinirSenha(dto);
        return ResponseEntity.ok().build();
    }
}
