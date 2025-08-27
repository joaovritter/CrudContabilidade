package com.joazao.crudContabilidade.controller;

import com.joazao.crudContabilidade.dto.CompraCreateDTO;
import com.joazao.crudContabilidade.dto.CompraDTO;
import com.joazao.crudContabilidade.dto.ItemCompraCreateDTO;
import com.joazao.crudContabilidade.model.Compra;
import com.joazao.crudContabilidade.model.Fornecedor;
import com.joazao.crudContabilidade.model.ItemCompra;
import com.joazao.crudContabilidade.model.Produto;
import com.joazao.crudContabilidade.model.FormaPagamento;
import com.joazao.crudContabilidade.repository.CompraRepository;
import com.joazao.crudContabilidade.repository.FornecedorRepository;
import com.joazao.crudContabilidade.repository.ItemCompraRepository;
import com.joazao.crudContabilidade.repository.ProdutoRepository;
import com.joazao.crudContabilidade.service.CompraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraController {
    private final CompraRepository compraRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemCompraRepository itemCompraRepository;
    private final CompraService compraService;

    public CompraController(CompraRepository compraRepository, FornecedorRepository fornecedorRepository, ProdutoRepository produtoRepository, ItemCompraRepository itemCompraRepository, CompraService compraService) {
        this.compraRepository = compraRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
        this.itemCompraRepository = itemCompraRepository;
        this.compraService = compraService;
    }

    @GetMapping
    public String listarCompras(Model model) {
        model.addAttribute("compras", compraRepository.findAll());
        return "compras/listar";
    }

    @GetMapping("/nova")
    public String novaCompra(Model model) {
        model.addAttribute("fornecedores", fornecedorRepository.findAllByOrderByNomeAsc());
        model.addAttribute("produtos", produtoRepository.findAll());
        model.addAttribute("compra", new Compra());
        return "compras/nova";
    }

    @PostMapping("/nova")
    public String salvarCompra(@RequestParam Long fornecedorId,
                              @RequestParam List<Long> produtoIds,
                              @RequestParam List<Integer> quantidades,
                              @RequestParam List<BigDecimal> valores,
                              @RequestParam FormaPagamento formaPagamento,
                              @RequestParam(required = false) Integer parcelas,
                              RedirectAttributes redirectAttributes) {
        try {
            Compra compra = new Compra();
            Fornecedor fornecedor = fornecedorRepository.findById(fornecedorId).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
            compra.setFornecedor(fornecedor);
            List<ItemCompra> itens = new ArrayList<>();
            BigDecimal valorTotal = BigDecimal.ZERO;
            for (int i = 0; i < produtoIds.size(); i++) {
                Produto produto = produtoRepository.findById(produtoIds.get(i)).orElseThrow();
                int qtd = quantidades.get(i);
                BigDecimal valorUnit = valores.get(i);
                BigDecimal valorItem = valorUnit.multiply(BigDecimal.valueOf(qtd));
                ItemCompra item = new ItemCompra();
                item.setProduto(produto);
                item.setQuantidade(qtd);
                item.setValorUnitario(valorUnit);
                item.setValorTotal(valorItem);
                item.setCompra(compra);
                itens.add(item);
                // Atualiza estoque
                produto.setEstoque(produto.getEstoque() + qtd);
                produtoRepository.save(produto);
                valorTotal = valorTotal.add(valorItem);
            }
            compra.setItensCompra(itens);
            // Calcular ICMS de crédito (17% do valor total)
            BigDecimal valorIcmsCredito = valorTotal.multiply(BigDecimal.valueOf(0.17));
            compra.setValorTotal(valorTotal);
            compra.setValorIcmsCredito(valorIcmsCredito);
            compra.setDataCompra(LocalDateTime.now());
            compra.setFormaPagamento(formaPagamento);
            if (formaPagamento == FormaPagamento.APRAZO && parcelas != null && parcelas > 1) {
                compra.setParcelas(parcelas);
                compra.setParcelasPagas(0);
            } else {
                compra.setParcelas(1);
                compra.setParcelasPagas(1);
            }
            compraRepository.save(compra);
            redirectAttributes.addFlashAttribute("mensagem", "Compra registrada com sucesso!");
            return "redirect:/compras";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao registrar compra: " + e.getMessage());
            return "redirect:/compras/nova";
        }
    }

    @GetMapping("/{id}")
    public String detalharCompra(@PathVariable Long id, Model model) {
        Compra compra = compraRepository.findById(id).orElseThrow(() -> new RuntimeException("Compra não encontrada"));
        model.addAttribute("compra", compra);
        return "compras/detalhar";
    }

    @PostMapping("/{id}/pagar-parcela")
    public String pagarParcela(@PathVariable Long id, @RequestParam(defaultValue = "1") int qtdParcelas, RedirectAttributes redirectAttributes) {
        Compra compra = compraRepository.findById(id).orElseThrow(() -> new RuntimeException("Compra não encontrada"));
        int parcelasRestantes = compra.getParcelas() - compra.getParcelasPagas();
        int pagar = Math.min(qtdParcelas, parcelasRestantes);
        if (compra.getSaldoDevedor().compareTo(BigDecimal.ZERO) > 0 && pagar > 0) {
            compra.setParcelasPagas(compra.getParcelasPagas() + pagar);
            compraRepository.save(compra);
            redirectAttributes.addFlashAttribute("mensagem", pagar + " parcela(s) paga(s) com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível pagar mais parcelas.");
        }
        return "redirect:/compras/" + id;
    }
} 