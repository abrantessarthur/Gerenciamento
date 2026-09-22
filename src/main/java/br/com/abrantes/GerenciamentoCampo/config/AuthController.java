package br.com.abrantes.GerenciamentoCampo.config;

import br.com.abrantes.GerenciamentoCampo.dto.request.LoginDto;
import br.com.abrantes.GerenciamentoCampo.dto.request.RegistroDto;
import br.com.abrantes.GerenciamentoCampo.dto.response.TokenResponseDTO;
import br.com.abrantes.GerenciamentoCampo.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegistroDto registerRequest) throws Exception {
        authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(
            @RequestBody @Valid LoginDto request
    ) {
        return ResponseEntity.ok(
                authenticationService.login(request)
        );
    }


}

