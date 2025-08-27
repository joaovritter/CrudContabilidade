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
public class VendaService {
    @Autowired
    private VendaRepository vendaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ItemVendaRepository itemVendaRepository;

    public List<VendaDTO> findAll() {
        return vendaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<VendaDTO> findById(Long id) {
        return vendaRepository.findById(id).map(this::toDTO);
    }

    public VendaDTO save(VendaCreateDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.clienteId()).orElseThrow();
        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setTipoVenda(dto.tipoVenda());
        venda.setNumeroParcelas(dto.numeroParcelas());
        venda.setValorTotal(dto.valorTotal());
        venda.setValorIcmsDebito(dto.valorIcmsDebito());
        List<ItemVenda> itens = dto.itens().stream().map(itemDTO -> {
            Produto produto = produtoRepository.findById(itemDTO.produtoId()).orElseThrow();
            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(itemDTO.quantidade());
            item.setPrecoUnitario(itemDTO.precoUnitario());
            item.calcularTotais();
            item.setVenda(venda);
            return item;
        }).collect(Collectors.toList());
        venda.setItens(itens);
        Venda saved = vendaRepository.save(venda);
        return toDTO(saved);
    }

    public void delete(Long id) {
        vendaRepository.deleteById(id);
    }

    public double calcularIcmsAPagar(Long vendaId) {
        Venda venda = vendaRepository.findById(vendaId).orElseThrow();
        double icmsDebito = venda.getValorIcmsDebito();
        
        // Para simplificar, vamos calcular baseado no preço de compra dos produtos
        // Em um sistema real, você buscaria as compras específicas do período
        double icmsCreditoTotal = venda.getItens().stream()
            .mapToDouble(item -> {
                Produto produto = item.getProduto();
                if (produto != null) {
                    // ICMS de crédito = preço de compra * quantidade * 17%
                    return produto.getPrecoCompra() * item.getQuantidade() * 0.17;
                }
                return 0.0;
            })
            .sum();
        
        return icmsDebito - icmsCreditoTotal;
    }

    private VendaDTO toDTO(Venda venda) {
        return new VendaDTO(
                venda.getId(),
                venda.getCliente() != null ? venda.getCliente().getId() : null,
                venda.getCliente() != null ? venda.getCliente().getNome() : null,
                venda.getCliente() != null ? venda.getCliente().getCpf() : null,
                venda.getDataVenda(),
                venda.getTipoVenda(),
                venda.getNumeroParcelas(),
                venda.getValorTotal(),
                venda.getValorIcmsDebito(),
                venda.getItens().stream().map(this::toItemDTO).collect(Collectors.toList())
        );
    }

    private ItemVendaDTO toItemDTO(ItemVenda item) {
        return new ItemVendaDTO(
                item.getId(),
                item.getProduto() != null ? item.getProduto().getId() : null,
                item.getProduto() != null ? item.getProduto().getNome() : null,
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getValorIcms(),
                item.getValorTotal()
        );
    }
} 