package com.joazao.crudContabilidade.service;

import com.joazao.crudContabilidade.dto.CapitalSocialDTO;
import com.joazao.crudContabilidade.dto.CapitalSocialCreateDTO;
import com.joazao.crudContabilidade.model.CapitalSocial;
import com.joazao.crudContabilidade.repository.CapitalSocialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CapitalSocialService {
    @Autowired
    private CapitalSocialRepository capitalSocialRepository;
    @Autowired
    private CaixaService caixaService;

    public List<CapitalSocialDTO> findAll() {
        return capitalSocialRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<CapitalSocialDTO> findById(Long id) {
        return capitalSocialRepository.findById(id).map(this::toDTO);
    }

    public CapitalSocialDTO save(CapitalSocialCreateDTO dto) {
        CapitalSocial capital = new CapitalSocial();
        capital.setValorAbertura(dto.valorAbertura());
        capital.setDataAbertura(dto.dataAbertura());
        CapitalSocial saved = capitalSocialRepository.save(capital);
        
        // Registrar movimentação no caixa como entrada (capital social aumenta o caixa)
        caixaService.registrarCapitalSocial(saved);
        
        return toDTO(saved);
    }

    public CapitalSocialDTO update(Long id, CapitalSocialCreateDTO dto) {
        CapitalSocial capital = capitalSocialRepository.findById(id).orElseThrow();
        capital.setValorAbertura(dto.valorAbertura());
        capital.setDataAbertura(dto.dataAbertura());
        CapitalSocial saved = capitalSocialRepository.save(capital);
        return toDTO(saved);
    }

    public void delete(Long id) {
        capitalSocialRepository.deleteById(id);
    }

    private CapitalSocialDTO toDTO(CapitalSocial capital) {
        return new CapitalSocialDTO(
                capital.getId(),
                capital.getValorAbertura(),
                capital.getDataAbertura()
        );
    }
} 