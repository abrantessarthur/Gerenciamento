package br.com.abrantes.GerenciamentoCampo.entity;

import br.com.abrantes.GerenciamentoCampo.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "campos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeDoCampo;

    @Enumerated(EnumType.STRING)
    private Status status;

    private BigDecimal valor;

    private LocalDateTime horarioAbertura;

    private LocalDateTime horarioFechamento;
}
