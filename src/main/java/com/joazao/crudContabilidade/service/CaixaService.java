package com.joazao.crudContabilidade.service;

import com.joazao.crudContabilidade.dto.CaixaCreateDTO;
import com.joazao.crudContabilidade.dto.CaixaDTO;
import com.joazao.crudContabilidade.model.Caixa;
import com.joazao.crudContabilidade.model.Patrimonio;
import com.joazao.crudContabilidade.model.CapitalSocial;
import com.joazao.crudContabilidade.model.enums.TipoMovimentacao;
import com.joazao.crudContabilidade.repository.CaixaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CaixaService {

    @Autowired
    private CaixaRepository caixaRepository;

    public List<CaixaDTO> findAll() {
        return caixaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<CaixaDTO> findById(Long id) {
        return caixaRepository.findById(id).map(this::toDTO);
    }

    public CaixaDTO save(CaixaCreateDTO createDTO) {
        Caixa caixa = toEntity(createDTO);
        Caixa saved = caixaRepository.save(caixa);
        return toDTO(saved);
    }

    public void delete(Long id) {
        caixaRepository.deleteById(id);
    }

    public CaixaDTO update(Long id, CaixaCreateDTO createDTO) {
        Caixa caixa = caixaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
        
        caixa.setData(createDTO.data());
        caixa.setTipo(createDTO.tipo());
        caixa.setValor(createDTO.valor());
        caixa.setDescricao(createDTO.descricao());
        caixa.setRelacionadoId(createDTO.relacionadoId());
        caixa.setCriadoEm(createDTO.criadoEm());
        
        Caixa saved = caixaRepository.save(caixa);
        return toDTO(saved);
    }

    private CaixaDTO toDTO(Caixa caixa) {
        return new CaixaDTO(
                caixa.getId(),
                caixa.getData(),
                caixa.getTipo(),
                caixa.getValor(),
                caixa.getDescricao(),
                caixa.getRelacionadoId(),
                caixa.getCriadoEm()
        );
    }

    private Caixa toEntity(CaixaCreateDTO dto) {
        Caixa caixa = new Caixa();
        caixa.setData(dto.data());
        caixa.setTipo(dto.tipo());
        caixa.setValor(dto.valor());
        caixa.setDescricao(dto.descricao());
        caixa.setRelacionadoId(dto.relacionadoId());
        caixa.setCriadoEm(dto.criadoEm());
        return caixa;
    }

    public void registrarCompraAPrazo(Patrimonio patrimonio) {
        CaixaCreateDTO createDTO = new CaixaCreateDTO(
                patrimonio.getDataAquisicao(),
                TipoMovimentacao.SAIDA,
                patrimonio.getValorAquisicao(),
                "Compra a prazo de " + patrimonio.getNome(),
                patrimonio.getId(),
                java.time.LocalDateTime.now()
        );
        save(createDTO);
    }


    public void registrarCompraAVista(Patrimonio patrimonio) {
        CaixaCreateDTO createDTO = new CaixaCreateDTO(
                patrimonio.getDataAquisicao(),
                TipoMovimentacao.SAIDA,
                patrimonio.getValorAquisicao(),
                "Compra à vista de " + patrimonio.getNome(),
                patrimonio.getId(),
                java.time.LocalDateTime.now()
        );
        save(createDTO);

    }

    public void registrarCapitalSocial(CapitalSocial capitalSocial) {
        CaixaCreateDTO createDTO = new CaixaCreateDTO(
                capitalSocial.getDataAbertura(),
                TipoMovimentacao.ENTRADA,
                capitalSocial.getValorAbertura(),
                "Capital Social - " + capitalSocial.getValorAbertura(),
                capitalSocial.getId(),
                java.time.LocalDateTime.now()
        );
        save(createDTO);
    }

    /**
     * Calcula o saldo total do caixa, considerando entradas e saídas.
     * Utiliza a biblioteca Java Streams Math para somar os valores.
     * @return O saldo total do caixa.
     */
    public BigDecimal getSaldo() {
        return caixaRepository.findAll().stream()
                .map(c -> c.getTipo() == TipoMovimentacao.ENTRADA ? c.getValor() : c.getValor().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<CaixaDTO> findAllOrderByDataDesc() {
        return caixaRepository.findAll().stream()
            .sorted((a, b) -> b.getData().compareTo(a.getData()))
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<CaixaDTO> findEntradasOrderByDataDesc() {
        return caixaRepository.findAll().stream()
            .filter(c -> c.getTipo() == TipoMovimentacao.ENTRADA)
            .sorted((a, b) -> b.getData().compareTo(a.getData()))
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<CaixaDTO> findSaidasOrderByDataDesc() {
        return caixaRepository.findAll().stream()
            .filter(c -> c.getTipo() == TipoMovimentacao.SAIDA)
            .sorted((a, b) -> b.getData().compareTo(a.getData()))
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
}