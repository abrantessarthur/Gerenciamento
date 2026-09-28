package br.com.abrantes.GerenciamentoCampo.controller;


import br.com.abrantes.GerenciamentoCampo.dto.request.EntrarListaEsperaDto;
import br.com.abrantes.GerenciamentoCampo.dto.response.ListaEsperaDto;
import br.com.abrantes.GerenciamentoCampo.service.ListaDeEsperaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lista-espera")
public class ListaDeEsperaController {

    private final ListaDeEsperaService listaDeEsperaService;

    @PostMapping
    public ResponseEntity<ListaEsperaDto> entrarNaListaDeEspera(@Valid @RequestBody EntrarListaEsperaDto entrarListaEsperaDto){
        ListaEsperaDto entrouNaLista =
                listaDeEsperaService.entrarNaLista(entrarListaEsperaDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entrouNaLista);
    }
}
