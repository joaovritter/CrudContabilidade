package com.joazao.crudContabilidade.service;

import com.joazao.crudContabilidade.model.Compra;
import com.joazao.crudContabilidade.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class BalancoPatrimonialService {
    
    private final CaixaService caixaService;
    private final ProdutoRepository produtoRepository;
    private final VendaRepository vendaRepository;
    private final PatrimonioRepository patrimonioRepository;
    private final CompraRepository compraRepository;
    private final CapitalSocialRepository capitalSocialRepository;
    
    public BalancoPatrimonialService(CaixaService caixaService,
                                   ProdutoRepository produtoRepository,
                                   VendaRepository vendaRepository,
                                   PatrimonioRepository patrimonioRepository,
                                   CompraRepository compraRepository,
                                   CapitalSocialRepository capitalSocialRepository) {
        this.caixaService = caixaService;
        this.produtoRepository = produtoRepository;
        this.vendaRepository = vendaRepository;
        this.patrimonioRepository = patrimonioRepository;
        this.compraRepository = compraRepository;
        this.capitalSocialRepository = capitalSocialRepository;
    }
    
    // ========== ATIVOS ==========
    
    /**
     * Saldo atual do caixa
     */
    public BigDecimal getSaldoCaixa() {
        return caixaService.getSaldo();
    }
    
    /**
     * Valor total dos estoques (produtos em estoque)
     */
    public BigDecimal getValorEstoques() {
        return produtoRepository.findAll().stream()
                .map(produto -> BigDecimal.valueOf(produto.getPrecoCompra() * produto.getEstoque()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Contas a receber (vendas a prazo não pagas)
     */
    public BigDecimal getContasReceber() {
        return vendaRepository.findAll().stream()
                .filter(venda -> "APRAZO".equals(venda.getTipoVenda()))
                .map(venda -> BigDecimal.valueOf(venda.getValorTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Bens patrimoniais (imóveis, veículos, máquinas, etc.)
     */
    public BigDecimal getBensPatrimoniais() {
        return patrimonioRepository.findAll().stream()
                .map(patrimonio -> patrimonio.getValorAquisicao())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Total dos ativos
     */
    public BigDecimal getTotalAtivo() {
        return getSaldoCaixa()
                .add(getValorEstoques())
                .add(getContasReceber())
                .add(getBensPatrimoniais())
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    // ========== PASSIVOS ==========
    
    /**
     * Contas a pagar (compras a prazo não pagas)
     */
    public BigDecimal getContasPagar() {
        return compraRepository.findAll().stream()
                .filter(compra -> compra.getFormaPagamento().name().equals("APRAZO"))
                .map(Compra::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Total dos passivos
     */
    public BigDecimal getTotalPassivo() {
        return getContasPagar().setScale(2, RoundingMode.HALF_UP);
    }
    
    // ========== PATRIMÔNIO LÍQUIDO ==========
    
    /**
     * Capital social total
     */
    public BigDecimal getCapitalSocial() {
        return capitalSocialRepository.findAll().stream()
                .map(capital -> capital.getValorAbertura())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Lucros ou prejuízos acumulados (Ativo Total - Passivo Total - Capital Social)
     */
    public BigDecimal getLucrosPrejuizosAcumulados() {
        return getTotalAtivo()
                .subtract(getTotalPassivo())
                .subtract(getCapitalSocial())
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Total do patrimônio líquido (Capital Social + Lucros/Prejuízos Acumulados)
     */
    public BigDecimal getTotalPatrimonioLiquido() {
        return getCapitalSocial()
                .add(getLucrosPrejuizosAcumulados())
                .setScale(2, RoundingMode.HALF_UP);
    }
} 