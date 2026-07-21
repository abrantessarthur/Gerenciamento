package br.com.abrantes.GerenciamentoCampo.exception;

public class UsuarioNaoEncontradoException extends NotFoundException {
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
}
