package br.com.abrantes.GerenciamentoCampo.dto;

import br.com.abrantes.GerenciamentoCampo.entity.CampoEntity;
import br.com.abrantes.GerenciamentoCampo.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

public record CampoDto(
        @NotBlank
        String nomeDoCampo,

        @NotNull
        Status status,

        @NotNull
        BigDecimal valor,

        @NotNull
        LocalDateTime horarioAbertura,

        @NotNull
        LocalDateTime horarioFechamento

) {
        public CampoDto(CampoEntity campo) {
                this(
                        campo.getNomeDoCampo(),
                        campo.getStatus(),
                        campo.getValor(),
                        campo.getHorarioAbertura(),
                        campo.getHorarioFechamento()
                );
        }

}
