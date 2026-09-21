package br.com.abrantes.GerenciamentoCampo.service;

import br.com.abrantes.GerenciamentoCampo.entity.CampoEntity;
import br.com.abrantes.GerenciamentoCampo.dto.request.CampoDto;
import br.com.abrantes.GerenciamentoCampo.exception.BadRequestException;
import br.com.abrantes.GerenciamentoCampo.exception.CampoNaoEncontradoException;
import br.com.abrantes.GerenciamentoCampo.repository.CampoRepository;
import br.com.abrantes.GerenciamentoCampo.repository.CamposProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampoService {
    private final CampoRepository campoRepository;

    public CampoDto criarCampo(CampoDto campo) {
        if (campoRepository.existsByNomeDoCampo(campo.nomeDoCampo())) {
            throw new BadRequestException("Campo já cadastrado com esse nome");
        }

        CampoEntity novoCampo = CampoEntity.builder()
                .nomeDoCampo(campo.nomeDoCampo())
                .status(campo.status())
                .valor(campo.valor())
                .build();

        CampoEntity salva = campoRepository.save(novoCampo);
        return new CampoDto(salva);
    }

    @Transactional(readOnly = true)
    public List<CampoDto> listarCampos() {
        List<CampoEntity> campos = campoRepository.findAll();
        return campos.stream()
                .map(CampoDto::new)
                .toList();
    }

    public void excluirCampo(Long id) {
        if (!campoRepository.existsById(id)) {
            throw new CampoNaoEncontradoException("Campo inexistente");
        }
        campoRepository.deleteById(id);
    }

    public CampoDto alterarCampo(Long id, CampoDto campo) {
        CampoEntity novoCampo = campoRepository.findById(id)
                .orElseThrow(() -> new CampoNaoEncontradoException("Campo não existe"));

        novoCampo.setNomeDoCampo(campo.nomeDoCampo());
        novoCampo.setStatus(campo.status());
        novoCampo.setValor(campo.valor());

        CampoEntity salva = campoRepository.save(novoCampo);
        return new CampoDto(salva);
    }

    public Page<CamposProjection> getAllCamposPageable(Integer page, Integer size){
        return campoRepository.getAllCamposPage(PageRequest.of(page, size));
    }


}
