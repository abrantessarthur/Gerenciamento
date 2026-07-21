package br.com.abrantes.GerenciamentoCampo.repository;

import br.com.abrantes.GerenciamentoCampo.enums.Status;

import java.math.BigDecimal;

public interface CamposProjection {

    Long getId();
    String nomeDoCampo();
    Status getStatus();
}
