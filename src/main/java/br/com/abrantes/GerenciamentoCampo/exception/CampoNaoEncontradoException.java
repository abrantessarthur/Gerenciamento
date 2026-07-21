package br.com.abrantes.GerenciamentoCampo.exception;

public class CampoNaoEncontradoException extends NotFoundException {
    public CampoNaoEncontradoException(String message) {
        super(message);
    }
}
