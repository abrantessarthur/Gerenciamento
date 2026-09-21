package br.com.abrantes.GerenciamentoCampo.controller;

import br.com.abrantes.GerenciamentoCampo.dto.request.CampoDto;
import br.com.abrantes.GerenciamentoCampo.dto.request.CriarReservaDto;
import br.com.abrantes.GerenciamentoCampo.dto.response.HorarioDisponivelDto;
import br.com.abrantes.GerenciamentoCampo.dto.request.ReservaDto;
import br.com.abrantes.GerenciamentoCampo.enums.StatusReserva;
import br.com.abrantes.GerenciamentoCampo.repository.ReservasProjection;
import br.com.abrantes.GerenciamentoCampo.service.ReservaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    @PostMapping
    @Transactional
    public ResponseEntity<ReservaDto> reservarCampo(@Valid @RequestBody CriarReservaDto reserva){
        ReservaDto reservaCriada =
                reservaService.reservarCampo(reserva);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservaCriada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirReserva(@PathVariable Long id){
        reservaService.deletarReserva(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/campo/{campoId}")
    public ResponseEntity<List<ReservaDto>> buscarReserva(
            @PathVariable Long campoId,
            @RequestParam LocalDate data) {

        return ResponseEntity.ok(
                reservaService.getReservasDoCampo(data, campoId)
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaDto>> buscarReservaDoUsuario(
            @PathVariable Long usuarioId,
            @RequestParam LocalDate data) {

        return ResponseEntity.ok(
                reservaService.getReservasDoUsuario(data, usuarioId)
        );
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ReservaDto> alterarReserva(
            @PathVariable Long id,
            @RequestBody @Valid CriarReservaDto reserva) {

        return ResponseEntity.ok(
                reservaService.alterarAgendamento(id, reserva)
        );
    }

    @GetMapping("/page/{page}/size/{size}")
    public Page<ReservasProjection> getAllCamposPageable(@PathVariable Integer page,@PathVariable Integer size){
        return reservaService.getAllReservasPageable(page, size);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/campo-mais-usado")
    public ResponseEntity<CampoDto> getCampoMaisUsado() {
        return reservaService.obterCampoMaisReservado()
                .map(campo -> ResponseEntity.ok(campo))
                .orElse(ResponseEntity.noContent().build());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/campo-mais-lucrativo")
    public ResponseEntity<CampoDto> getCampoMaisLucrativo() {
        return reservaService.getCampoMaisLucrativo()
                .map(campo -> ResponseEntity.ok(campo))
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/campos-livres/{id}")
    public List<HorarioDisponivelDto> getHorariosDisponiveis(
            @PathVariable("id") Long campoId,
            @RequestParam LocalDate data) {
        return reservaService.getHorariosDisponiveis(campoId, data);
    }

    @GetMapping("/disponiveis")
    public List<CampoDto> getCamposDisponivelNoHorario(
            @RequestParam LocalDateTime horaInicio,
            @RequestParam LocalDateTime horaFim) {
        return reservaService.getCamposDisponiveisNoHorario(horaInicio, horaFim);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<ReservaDto> alterarStatusReserva(
            @PathVariable Long id,
            @RequestParam StatusReserva status) {
        return ResponseEntity.ok(reservaService.alterarStatusReserva(id, status));
    }
}
