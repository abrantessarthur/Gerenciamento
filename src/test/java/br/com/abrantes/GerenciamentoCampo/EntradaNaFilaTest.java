package br.com.abrantes.GerenciamentoCampo;

import br.com.abrantes.GerenciamentoCampo.dto.request.CriarReservaDto;
import br.com.abrantes.GerenciamentoCampo.dto.request.EntrarListaEsperaDto;
import br.com.abrantes.GerenciamentoCampo.dto.response.ListaEsperaDto;
import br.com.abrantes.GerenciamentoCampo.entity.CampoEntity;
import br.com.abrantes.GerenciamentoCampo.entity.UsuarioEntity;
import br.com.abrantes.GerenciamentoCampo.enums.Status;
import br.com.abrantes.GerenciamentoCampo.enums.StatusListaEspera;
import br.com.abrantes.GerenciamentoCampo.exception.BadRequestException;
import br.com.abrantes.GerenciamentoCampo.repository.CampoRepository;
import br.com.abrantes.GerenciamentoCampo.repository.ListaDeEsperaRepository;
import br.com.abrantes.GerenciamentoCampo.repository.ReservaRepository;
import br.com.abrantes.GerenciamentoCampo.repository.UsuarioRepository;
import br.com.abrantes.GerenciamentoCampo.service.ListaDeEsperaService;
import br.com.abrantes.GerenciamentoCampo.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class EntradaNaFilaTest {
    @Autowired
    private ListaDeEsperaService listaDeEsperaService;
    @Autowired
    private ListaDeEsperaRepository listaDeEsperaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CampoRepository campoRepository;
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ReservaRepository reservaRepository;

    private Long usuarioId1;
    private Long usuarioId2;
    private Long campoId;
    private LocalDate dataReserva;

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:8.4");

    @Test
    void deveIniciarMySqlContainer() {
        assertTrue(mysql.isRunning());
    }

    @BeforeEach
    void setUp(){
        listaDeEsperaRepository.deleteAllInBatch();
        reservaRepository.deleteAllInBatch();
        campoRepository.deleteAllInBatch();
        usuarioRepository.deleteAllInBatch();
        dataReserva = LocalDate.now().plusDays(1);

        UsuarioEntity usuarioSalvo1 = usuarioRepository.save(UsuarioEntity.builder()
                .nome("arthur")
                .email("arthur@gmail.com")
                .senha("senha123")
                .build());
        usuarioId1 = usuarioSalvo1.getId();

        UsuarioEntity usuarioSalvo2 = usuarioRepository.save(UsuarioEntity.builder()
                .nome("lucas")
                .email("lucas@gmail.com")
                .senha("lucas123")
                .build());
        usuarioId2 = usuarioSalvo2.getId();

        CampoEntity campoSalvo = campoRepository.save(CampoEntity.builder()
                .nomeDoCampo("Campo Concorrencia")
                .status(Status.DISPONIVEL)
                .valor(new BigDecimal("100.00"))
                .horarioAbertura(dataReserva.atTime(8, 0))
                .horarioFechamento(dataReserva.atTime(22, 0))
                .build());
        campoId = campoSalvo.getId();
    }

    @Test
    void deveAdicionarUsuarioNaListaQuandoHorarioEstiverOcupado(){
        CriarReservaDto criar = new CriarReservaDto(usuarioId1, campoId, dataReserva.atTime(10, 0), dataReserva.atTime(11, 0));
        reservaService.reservarCampo(criar, "reserva-inicial");
        EntrarListaEsperaDto entrar = new EntrarListaEsperaDto(usuarioId2, campoId, dataReserva.atTime(10, 0), dataReserva.atTime(11, 0));
        ListaEsperaDto resultado =
                listaDeEsperaService.entrarNaLista(entrar);

        assertEquals(StatusListaEspera.AGUARDANDO, resultado.status());
        assertNotNull(resultado.criadoEm());
        assertEquals(1L, listaDeEsperaRepository.count());
    }

    @Test
    void naoDeveAdicionarEntradaDuplicadaNaLista(){
        CriarReservaDto criar = new CriarReservaDto(usuarioId1, campoId, dataReserva.atTime(10, 0), dataReserva.atTime(11, 0));
        reservaService.reservarCampo(criar, "reserva-duplicidade");

        EntrarListaEsperaDto entrar = new EntrarListaEsperaDto(usuarioId2, campoId, dataReserva.atTime(10, 0), dataReserva.atTime(11, 0));
        listaDeEsperaService.entrarNaLista(entrar);

        assertThrows(
                BadRequestException.class,
                () -> listaDeEsperaService.entrarNaLista(entrar)
        );
        assertEquals(1L, listaDeEsperaRepository.count());
    }

    @Test
    void naoDeveAdicionarNaListaQuandoHorarioEstiverLivre(){
        EntrarListaEsperaDto entrar = new EntrarListaEsperaDto(usuarioId1, campoId, dataReserva.atTime(12, 0), dataReserva.atTime(13, 0));

        assertThrows(
                BadRequestException.class,
                () -> listaDeEsperaService.entrarNaLista(entrar)
        );
        assertEquals(0L, listaDeEsperaRepository.count());
    }
}
