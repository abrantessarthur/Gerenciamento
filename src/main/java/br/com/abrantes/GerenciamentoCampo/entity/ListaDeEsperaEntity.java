package br.com.abrantes.GerenciamentoCampo.entity;
import br.com.abrantes.GerenciamentoCampo.enums.StatusListaEspera;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "lista_de_espera")
public class ListaDeEsperaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "campo_id", nullable = false)
    private CampoEntity campo;

    private LocalDateTime horaInicio;

    private LocalDateTime horaFim;

    private LocalDateTime criadoEm;

    @Enumerated(EnumType.STRING)
    private StatusListaEspera status;
}
