package com.joazao.crudContabilidade.service;

import com.joazao.crudContabilidade.dto.ProdutoDTO;
import com.joazao.crudContabilidade.dto.ProdutoCreateDTO;
import com.joazao.crudContabilidade.model.Fornecedor;
import com.joazao.crudContabilidade.model.Produto;
import com.joazao.crudContabilidade.repository.FornecedorRepository;
import com.joazao.crudContabilidade.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private FornecedorRepository fornecedorRepository;

    public List<ProdutoDTO> findAll() {
        return produtoRepository.findByEstoqueGreaterThan(0).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ProdutoDTO> findAllIncludingZeroStock() {
        return produtoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<ProdutoDTO> findById(Long id) {
        return produtoRepository.findById(id).map(this::toDTO);
    }

    public ProdutoDTO save(ProdutoCreateDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedorId()).orElseThrow();
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setPrecoCompra(dto.precoCompra());
        produto.setPrecoVenda(dto.precoVenda());
        produto.setIcms(17.0); // ICMS fixo em 17%
        produto.setDebito(dto.precoCompra()); // Débito = preço de compra
        produto.setCredito(dto.precoCompra()); // Crédito = preço de compra
        produto.setEstoque(0); // Sempre inicia com estoque 0
        produto.setFornecedor(fornecedor);
        Produto saved = produtoRepository.save(produto);
        return toDTO(saved);
    }

    public ProdutoDTO update(Long id, ProdutoCreateDTO dto) {
        Produto produto = produtoRepository.findById(id).orElseThrow();
        Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedorId()).orElseThrow();
        produto.setNome(dto.nome());
        produto.setPrecoCompra(dto.precoCompra());
        produto.setPrecoVenda(dto.precoVenda());
        produto.setIcms(17.0); // ICMS fixo em 17%
        produto.setDebito(dto.precoCompra()); // Débito = preço de compra
        produto.setCredito(dto.precoCompra()); // Crédito = preço de compra
        // Mantém o estoque atual (não altera)
        produto.setFornecedor(fornecedor);
        Produto saved = produtoRepository.save(produto);
        return toDTO(saved);
    }

    public void delete(Long id) {
        produtoRepository.deleteById(id);
    }

    private ProdutoDTO toDTO(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getPrecoCompra(),
                produto.getPrecoVenda(),
                produto.getIcms(),
                produto.getDebito(),
                produto.getCredito(),
                produto.getEstoque(),
                produto.getFornecedor() != null ? produto.getFornecedor().getId() : null,
                produto.getFornecedor() != null ? produto.getFornecedor().getNome() : null
        );
    }
} 