package com.joazao.crudContabilidade.service;

import com.joazao.crudContabilidade.dto.FornecedorDTO;
import com.joazao.crudContabilidade.dto.FornecedorCreateDTO;
import com.joazao.crudContabilidade.model.Fornecedor;
import com.joazao.crudContabilidade.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public List<FornecedorDTO> findAll() {
        return fornecedorRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<FornecedorDTO> findById(Long id) {
        return fornecedorRepository.findById(id).map(this::toDTO);
    }

    public FornecedorDTO save(FornecedorCreateDTO dto) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCnpj(dto.cnpj());
        fornecedor.setNome(dto.nome());
        fornecedor.setCidade(dto.cidade());
        fornecedor.setEstado(dto.estado());
        Fornecedor saved = fornecedorRepository.save(fornecedor);
        return toDTO(saved);
    }

    public FornecedorDTO update(Long id, FornecedorCreateDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElseThrow();
        fornecedor.setCnpj(dto.cnpj());
        fornecedor.setNome(dto.nome());
        fornecedor.setCidade(dto.cidade());
        fornecedor.setEstado(dto.estado());
        Fornecedor saved = fornecedorRepository.save(fornecedor);
        return toDTO(saved);
    }

    public void delete(Long id) {
        fornecedorRepository.deleteById(id);
    }

    private FornecedorDTO toDTO(Fornecedor fornecedor) {
        return new FornecedorDTO(
                fornecedor.getId(),
                fornecedor.getCnpj(),
                fornecedor.getNome(),
                fornecedor.getCidade(),
                fornecedor.getEstado()
        );
    }
} 