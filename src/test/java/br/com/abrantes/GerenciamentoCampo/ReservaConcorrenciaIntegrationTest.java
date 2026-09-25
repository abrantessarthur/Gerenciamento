package br.com.abrantes.GerenciamentoCampo;

import br.com.abrantes.GerenciamentoCampo.dto.request.CriarReservaDto;
import br.com.abrantes.GerenciamentoCampo.entity.CampoEntity;
import br.com.abrantes.GerenciamentoCampo.entity.UsuarioEntity;
import br.com.abrantes.GerenciamentoCampo.enums.Status;
import br.com.abrantes.GerenciamentoCampo.exception.HorarioIndisponivelException;
import br.com.abrantes.GerenciamentoCampo.repository.CampoRepository;
import br.com.abrantes.GerenciamentoCampo.repository.ReservaRepository;
import br.com.abrantes.GerenciamentoCampo.repository.UsuarioRepository;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class ReservaConcorrenciaIntegrationTest {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private CampoRepository campoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Long usuarioId;
    private Long campoId;
    private LocalDate dataReserva;

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:8.4");

    @Test
    void deveIniciarMySqlContainer(){
        assertTrue(mysql.isRunning());
    }

    @BeforeEach
    void setUp(){
        reservaRepository.deleteAllInBatch();
        campoRepository.deleteAllInBatch();
        usuarioRepository.deleteAllInBatch();

        dataReserva = LocalDate.now().plusDays(1);

        UsuarioEntity usuarioSalvo = usuarioRepository.save(UsuarioEntity.builder()
                .nome("arthur")
                .email("arthur@gmail.com")
                .senha("senha123")
                .build());
        usuarioId = usuarioSalvo.getId();

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
    void deveCriarApenasUmaReservaQuandoHouverRequisicoesSimultaneas() throws InterruptedException {
        CriarReservaDto criar = new CriarReservaDto(usuarioId, campoId, dataReserva.atTime(10, 0), dataReserva.atTime(11, 0));
        ExecutorService executor = Executors.newFixedThreadPool(50);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicInteger sucessos = new AtomicInteger();
        AtomicInteger conflitos = new AtomicInteger();

        for (int i = 0; i < 50; i++) {
            executor.submit(() -> {
                try {
                    try {
                        countDownLatch.await();
                        reservaService.reservarCampo(criar);
                        sucessos.incrementAndGet();
                    } catch (HorarioIndisponivelException e) {
                        conflitos.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            });
        }
        countDownLatch.countDown();
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
        boolean finalizou = executor.isTerminated();
        assertTrue(finalizou);
        assertEquals(1, sucessos.get());
        assertEquals(49, conflitos.get());
        assertEquals(1L, reservaRepository.count());
    }

}
