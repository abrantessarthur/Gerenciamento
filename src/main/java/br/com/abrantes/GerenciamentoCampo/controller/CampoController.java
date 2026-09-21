package br.com.abrantes.GerenciamentoCampo.controller;
import br.com.abrantes.GerenciamentoCampo.repository.CamposProjection;
import br.com.abrantes.GerenciamentoCampo.service.CampoService;
import br.com.abrantes.GerenciamentoCampo.dto.request.CampoDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/campos")
@Validated
public class CampoController {
    private final CampoService campoService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @Transactional
    public ResponseEntity<CampoDto> cadastrarCampo(@Valid @RequestBody CampoDto campoDto) {
        campoService.criarCampo(campoDto);
        return ResponseEntity.ok().body(campoDto);
    }

    @GetMapping
    public List<CampoDto> listarCampo() {
        return campoService.listarCampos();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCampo(@PathVariable Long id) {
        campoService.excluirCampo(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CampoDto> alterarCampo(
            @PathVariable Long id,
            @Valid @RequestBody CampoDto campoDto) {

        campoService.alterarCampo(id, campoDto);
        return ResponseEntity.ok().body(campoDto);
    }

    @GetMapping("/page/{page}/size/{size}")
    public Page<CamposProjection> getAllCamposPageable(@PathVariable Integer page, Integer size){
        return campoService.getAllCamposPageable(page, size);
    }
}
