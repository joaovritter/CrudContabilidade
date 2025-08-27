package com.joazao.crudContabilidade.service;

import com.joazao.crudContabilidade.dto.*;
import com.joazao.crudContabilidade.model.*;
import com.joazao.crudContabilidade.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompraService {
    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private FornecedorRepository fornecedorRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ItemCompraRepository itemCompraRepository;

    public List<CompraDTO> findAll() {
        return compraRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<CompraDTO> findById(Long id) {
        return compraRepository.findById(id).map(this::toDTO);
    }

    public CompraDTO save(CompraCreateDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedorId()).orElseThrow();
        Compra compra = new Compra();
        compra.setFornecedor(fornecedor);
        compra.setValorTotal(dto.valorTotal());
        compra.setValorIcmsCredito(dto.valorIcmsCredito());
        compra.setDataCompra(dto.dataCompra());
        compra.setFormaPagamento(FormaPagamento.valueOf(dto.formaPagamento()));
        compra.setParcelas(dto.parcelas());
        compra.setParcelasPagas(dto.parcelasPagas());
        List<ItemCompra> itens = dto.itensCompra().stream().map(itemDTO -> {
            Produto produto = produtoRepository.findById(itemDTO.produtoId()).orElseThrow();
            ItemCompra item = new ItemCompra();
            item.setProduto(produto);
            item.setQuantidade(itemDTO.quantidade());
            item.setValorUnitario(itemDTO.valorUnitario());
            item.setValorTotal(itemDTO.valorTotal());
            item.setCompra(compra);
            return item;
        }).collect(Collectors.toList());
        compra.setItensCompra(itens);
        Compra saved = compraRepository.save(compra);
        return toDTO(saved);
    }

    public void delete(Long id) {
        compraRepository.deleteById(id);
    }

    private CompraDTO toDTO(Compra compra) {
        return new CompraDTO(
                compra.getId(),
                compra.getFornecedor() != null ? compra.getFornecedor().getId() : null,
                compra.getFornecedor() != null ? compra.getFornecedor().getNome() : null,
                compra.getItensCompra().stream().map(this::toItemDTO).collect(Collectors.toList()),
                compra.getValorTotal(),
                compra.getValorIcmsCredito(),
                compra.getDataCompra(),
                compra.getFormaPagamento() != null ? compra.getFormaPagamento().name() : null,
                compra.getParcelas(),
                compra.getParcelasPagas(),
                compra.getSaldoDevedor()
        );
    }

    private ItemCompraDTO toItemDTO(ItemCompra item) {
        return new ItemCompraDTO(
                item.getId(),
                item.getProduto() != null ? item.getProduto().getId() : null,
                item.getProduto() != null ? item.getProduto().getNome() : null,
                item.getQuantidade(),
                item.getValorUnitario(),
                item.getValorTotal()
        );
    }
} 