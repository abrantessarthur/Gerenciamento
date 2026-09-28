package br.com.abrantes.GerenciamentoCampo.service;

import br.com.abrantes.GerenciamentoCampo.dto.request.EntrarListaEsperaDto;
import br.com.abrantes.GerenciamentoCampo.dto.response.ListaEsperaDto;
import br.com.abrantes.GerenciamentoCampo.entity.CampoEntity;
import br.com.abrantes.GerenciamentoCampo.entity.ListaDeEsperaEntity;
import br.com.abrantes.GerenciamentoCampo.entity.UsuarioEntity;
import br.com.abrantes.GerenciamentoCampo.enums.StatusListaEspera;
import br.com.abrantes.GerenciamentoCampo.exception.BadRequestException;
import br.com.abrantes.GerenciamentoCampo.exception.NotFoundException;
import br.com.abrantes.GerenciamentoCampo.repository.CampoRepository;
import br.com.abrantes.GerenciamentoCampo.repository.ListaDeEsperaRepository;
import br.com.abrantes.GerenciamentoCampo.repository.ReservaRepository;
import br.com.abrantes.GerenciamentoCampo.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ListaDeEsperaService {

    private final ListaDeEsperaRepository listaDeEsperaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CampoRepository campoRepository;
    private final ReservaRepository  reservaRepository;

    @Transactional
    public ListaEsperaDto entrarNaLista(EntrarListaEsperaDto entrarListaEsperaDto){
        if (!entrarListaEsperaDto.horaInicio().isBefore(entrarListaEsperaDto.horaFim())) {
            throw new BadRequestException(
                    "A hora de início deve ser anterior à hora de fim."
            );
        }
        UsuarioEntity usuario = usuarioRepository
                .findById(entrarListaEsperaDto.usuarioId())
                .orElseThrow(()->
                        new NotFoundException("Usuário não encontrado")
                );
        CampoEntity campo = campoRepository
                .findById(entrarListaEsperaDto.campoId())
                .orElseThrow(()->
                        new NotFoundException("Campo não encontrado")
                );

        boolean conflito = reservaRepository.existsOverlappingReserva(entrarListaEsperaDto.campoId(),
                entrarListaEsperaDto.horaInicio(),
                entrarListaEsperaDto.horaFim(),
                null);

        if(!conflito){
            throw new BadRequestException("Faça uma reserva diretamente");
        }

        if (listaDeEsperaRepository.existsByUsuario_IdAndCampo_IdAndHoraInicioAndHoraFimAndStatus(
                entrarListaEsperaDto.usuarioId(),
                entrarListaEsperaDto.campoId(),
                entrarListaEsperaDto.horaInicio(),
                entrarListaEsperaDto.horaFim(),
                StatusListaEspera.AGUARDANDO
        )){
            throw new BadRequestException("Já existe um mesmo usuário aguardando nesse campo e intervalo");
        }
        ListaDeEsperaEntity listaDeEsperaEntity = ListaDeEsperaEntity.builder()
                .usuario(usuario)
                .campo(campo)
                .horaFim(entrarListaEsperaDto.horaFim())
                .horaInicio(entrarListaEsperaDto.horaInicio())
                .status(StatusListaEspera.AGUARDANDO)
                .criadoEm(LocalDateTime.now())
                .build();

        ListaDeEsperaEntity salvo =  listaDeEsperaRepository.save(listaDeEsperaEntity);
        return new ListaEsperaDto(salvo);
    }


}
