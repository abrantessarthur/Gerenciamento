package br.com.abrantes.GerenciamentoCampo.service;

import br.com.abrantes.GerenciamentoCampo.dto.CampoDto;
import br.com.abrantes.GerenciamentoCampo.dto.CriarReservaDto;
import br.com.abrantes.GerenciamentoCampo.dto.HorarioDisponivelDto;
import br.com.abrantes.GerenciamentoCampo.dto.ReservaDto;
import br.com.abrantes.GerenciamentoCampo.entity.CampoEntity;
import br.com.abrantes.GerenciamentoCampo.entity.ReservaEntity;
import br.com.abrantes.GerenciamentoCampo.entity.UsuarioEntity;
import br.com.abrantes.GerenciamentoCampo.enums.Status;
import br.com.abrantes.GerenciamentoCampo.enums.StatusReserva;
import br.com.abrantes.GerenciamentoCampo.exception.*;
import br.com.abrantes.GerenciamentoCampo.repository.CampoRepository;
import br.com.abrantes.GerenciamentoCampo.repository.ReservaRepository;
import br.com.abrantes.GerenciamentoCampo.repository.ReservasProjection;
import br.com.abrantes.GerenciamentoCampo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CampoRepository campoRepository;

    public ReservaDto reservarCampo(
            CriarReservaDto reservaDto
    ) {
        if (!reservaDto.horaInicio().isBefore(reservaDto.horaFim())) {
            throw new BadRequestException(
                    "A hora de início deve ser anterior à hora de fim."
            );
        }

        boolean conflito = reservaRepository.existsOverlappingReserva(
                reservaDto.campoId(),
                reservaDto.horaInicio(),
                reservaDto.horaFim(),
                null
        );

        if (conflito) {
            throw new HorarioIndisponivelException(
                    "Horário indisponível para este campo."
            );
        }


        UsuarioEntity usuario = usuarioRepository
                .findById(reservaDto.usuarioId())
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException(
                                "Usuário não encontrado."
                        )
                );


        CampoEntity campo = campoRepository
                .findById(reservaDto.campoId())
                .orElseThrow(() ->
                        new CampoNaoEncontradoException(
                                "Campo não encontrado."
                        )
                );

        validarHorarioFuncionamento(campo, reservaDto.horaInicio(), reservaDto.horaFim());

        BigDecimal valorCalculado = calcularValorReserva(
                campo,
                reservaDto.horaInicio(),
                reservaDto.horaFim()
        );

        ReservaEntity reserva = new ReservaEntity();
        reserva.setUsuario(usuario);
        reserva.setCampo(campo);
        reserva.setHoraInicio(reservaDto.horaInicio());
        reserva.setHoraFim(reservaDto.horaFim());
        reserva.setValor(valorCalculado);
        reserva.setStatus(StatusReserva.PENDENTE);
        ReservaEntity salva = reservaRepository.save(reserva);

        return new ReservaDto(salva);
    }


    public void deletarReserva(Long id) {

        ReservaEntity reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada."));

        reservaRepository.delete(reserva);
    }

    public List<ReservaDto> getReservasDoCampo(LocalDate data, Long campoId) {

        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(23, 59, 59);

        return reservaRepository.findByCampo_IdAndHoraInicioBetween(
                        campoId,
                        inicio,
                        fim
                ).stream()
                .map(ReservaDto::new)
                .toList();
    }

    public List<ReservaDto> getReservasDoUsuario(LocalDate data, Long usuarioId) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(23, 59, 59);

        return reservaRepository.findByUsuario_IdAndHoraInicioBetween(
                usuarioId,
                inicio,
                fim
        ).stream()
                .map(ReservaDto::new)
                .toList();
    }

    @Transactional
    public ReservaDto alterarAgendamento(
            Long id,
            CriarReservaDto novaReservaDto
    ) {
        ReservaEntity reserva = reservaRepository.findById(id)
                .orElseThrow(() ->
                        new ReservaNaoEncontradaException(
                                "Reserva não encontrada."
                        )
                );

        if (!novaReservaDto.horaInicio()
                .isBefore(novaReservaDto.horaFim())) {
            throw new BadRequestException(
                    "A hora de início deve ser anterior à hora de fim."
            );
        }

        CampoEntity campo = campoRepository
                .findById(novaReservaDto.campoId())
                .orElseThrow(() ->
                        new CampoNaoEncontradoException(
                                "Campo não encontrado."
                        )
                );

        UsuarioEntity usuario = usuarioRepository
                .findById(novaReservaDto.usuarioId())
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException(
                                "Usuário não encontrado."
                        )
                );

        boolean conflito = reservaRepository.existsOverlappingReserva(
                novaReservaDto.campoId(),
                novaReservaDto.horaInicio(),
                novaReservaDto.horaFim(),
                id
        );

        if (conflito) {
            throw new HorarioIndisponivelException(
                    "Horário indisponível para este campo."
            );
        }

        BigDecimal novoValor = calcularValorReserva(
                campo,
                novaReservaDto.horaInicio(),
                novaReservaDto.horaFim()
        );

        reserva.setCampo(campo);
        reserva.setUsuario(usuario);
        reserva.setHoraInicio(novaReservaDto.horaInicio());
        reserva.setHoraFim(novaReservaDto.horaFim());
        reserva.setValor(novoValor);

        ReservaEntity salva = reservaRepository.save(reserva);

        return new ReservaDto(salva);
    }

    public Page<ReservasProjection> getAllReservasPageable(Integer page, Integer size){
        return reservaRepository.getAllReservasPageable(PageRequest.of(page, size));
    }

    public Optional<CampoDto> obterCampoMaisReservado() {
        return reservaRepository
                .findCampoMaisUsado(StatusReserva.CONFIRMADA, PageRequest.of(0, 1))
                .stream()
                .findFirst();
    }

    public Optional<CampoDto> getCampoMaisLucrativo(){
        return reservaRepository.findCampoMaisLucrativo(PageRequest.of(0, 1))
                .stream()
                .findFirst();
    }

    private BigDecimal calcularValorReserva(
            CampoEntity campo,
            LocalDateTime horaInicio,
            LocalDateTime horaFim
    ) {
        long minutos = Duration.between(horaInicio, horaFim).toMinutes();

        if (minutos <= 0) {
            throw new BadRequestException(
                    "A duração da reserva deve ser maior que zero."
            );
        }

        BigDecimal duracaoEmHoras = BigDecimal.valueOf(minutos)
                .divide(
                        BigDecimal.valueOf(60),
                        4,
                        RoundingMode.HALF_UP
                );

        return campo.getValor()
                .multiply(duracaoEmHoras)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public List<HorarioDisponivelDto> getHorariosDisponiveis(Long campoId, LocalDate data) {
        LocalDateTime abertura = data.atTime(8, 0);
        LocalDateTime fechamento = data.atTime(22, 0);

        List<ReservaEntity> reservas = reservaRepository.findReservasAtivasDoCampoNoDia(
                campoId, data.atStartOfDay(), data.atTime(23, 59, 59)
        );

        List<HorarioDisponivelDto> livres = new ArrayList<>();
        LocalDateTime cursor = abertura;

        for (ReservaEntity r : reservas) {
            if (r.getHoraInicio().isAfter(cursor)) {
                livres.add(new HorarioDisponivelDto(cursor, r.getHoraInicio()));
            }
            if (r.getHoraFim().isAfter(cursor)) {
                cursor = r.getHoraFim();
            }
        }

        if (cursor.isBefore(fechamento)) {
            livres.add(new HorarioDisponivelDto(cursor, fechamento));
        }

        return livres;
    }

    public List<CampoDto> getCamposDisponiveisNoHorario(LocalDateTime horaInicio, LocalDateTime horaFim) {
        return reservaRepository.findCamposDisponiveisNoHorario(horaInicio, horaFim)
                .stream()
                .map(CampoDto::new)
                .toList();
    }

    public ReservaDto alterarStatusReserva(Long id, StatusReserva statusReserva) {
        ReservaEntity reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNaoEncontradaException("Reserva não encontrada."));

        reserva.setStatus(statusReserva);

        ReservaEntity salva = reservaRepository.save(reserva);
        return new ReservaDto(salva);
    }

    private void validarHorarioFuncionamento(CampoEntity campo, LocalDateTime horaInicio, LocalDateTime horaFim) {
        LocalTime inicio = horaInicio.toLocalTime();
        LocalTime fim = horaFim.toLocalTime();

        if (inicio.isBefore(LocalTime.from(campo.getHorarioAbertura()))
                || fim.isAfter(LocalTime.from(campo.getHorarioFechamento()))) {

            throw new HorarioIndisponivelException(
                    "A reserva está fora do horário de funcionamento."
            );
        }
    }
}
