package br.com.abrantes.GerenciamentoCampo.entity;

import br.com.abrantes.GerenciamentoCampo.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "reserva")
public class ReservaEntity {

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

    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private StatusReserva status;

    @Column(value = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;
}
