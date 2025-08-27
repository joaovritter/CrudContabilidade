package com.joazao.crudContabilidade.service;

import com.joazao.crudContabilidade.dto.PatrimonioDTO;
import com.joazao.crudContabilidade.dto.PatrimonioCreateDTO;
import com.joazao.crudContabilidade.model.Patrimonio;
import com.joazao.crudContabilidade.model.Fornecedor;
import com.joazao.crudContabilidade.model.TipoBem;
import com.joazao.crudContabilidade.repository.PatrimonioRepository;
import com.joazao.crudContabilidade.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatrimonioService {
    @Autowired
    private PatrimonioRepository patrimonioRepository;
    @Autowired
    private FornecedorRepository fornecedorRepository;
    @Autowired
    private CaixaService caixaService;

    public List<PatrimonioDTO> findAll() {
        return patrimonioRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<PatrimonioDTO> findById(Long id) {
        return patrimonioRepository.findById(id).map(this::toDTO);
    }

    public PatrimonioDTO save(PatrimonioCreateDTO dto) {
        Patrimonio patrimonio = toEntity(dto);
        Patrimonio saved = patrimonioRepository.save(patrimonio);

        // Registrar movimentação no caixa para todos os patrimônios
        // Se tem parcelas, é considerado a prazo, senão é à vista
        if (dto.parcelasTotais() != null && dto.parcelasTotais() > 0) {
            caixaService.registrarCompraAPrazo(saved);
        } else {
            caixaService.registrarCompraAVista(saved);
        }
        
        return toDTO(saved);
    }

    public PatrimonioDTO update(Long id, PatrimonioCreateDTO dto) {
        Patrimonio patrimonio = patrimonioRepository.findById(id).orElseThrow();
        Patrimonio atualizado = toEntity(dto);
        atualizado.setId(id);
        atualizado = patrimonioRepository.save(atualizado);
        return toDTO(atualizado);
    }

    public void delete(Long id) {
        patrimonioRepository.deleteById(id);
    }

    private PatrimonioDTO toDTO(Patrimonio patrimonio) {
        return new PatrimonioDTO(
                patrimonio.getId(),
                patrimonio.getNome(),
                patrimonio.getDescricao(),
                patrimonio.getValorAquisicao(),
                patrimonio.getDataAquisicao(),
                patrimonio.getTipoBem() != null ? patrimonio.getTipoBem().name() : null,
                patrimonio.getValorEntrada(),
                patrimonio.getParcelasTotais(),
                patrimonio.getParcelasPagas(),
                patrimonio.getValorParcela(),
                patrimonio.getDataVencimentoPrimeiraParcela(),
                patrimonio.getFornecedor() != null ? patrimonio.getFornecedor().getId() : null,
                patrimonio.getFornecedor() != null ? patrimonio.getFornecedor().getNome() : null
        );
    }

    private Patrimonio toEntity(PatrimonioCreateDTO dto) {
        Patrimonio patrimonio = new Patrimonio();
        patrimonio.setNome(dto.nome());
        patrimonio.setDescricao(dto.descricao());
        patrimonio.setValorAquisicao(dto.valorAquisicao());
        patrimonio.setDataAquisicao(dto.dataAquisicao());
        patrimonio.setTipoBem(dto.tipoBem() != null ? TipoBem.valueOf(dto.tipoBem()) : null);
        patrimonio.setValorEntrada(dto.valorEntrada());
        patrimonio.setParcelasTotais(dto.parcelasTotais());
        patrimonio.setParcelasPagas(dto.parcelasPagas());
        patrimonio.setValorParcela(dto.valorParcela());
        patrimonio.setDataVencimentoPrimeiraParcela(dto.dataVencimentoPrimeiraParcela());
        if (dto.fornecedorId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedorId()).orElse(null);
            patrimonio.setFornecedor(fornecedor);
        }
        return patrimonio;
    }
} 