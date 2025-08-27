package com.joazao.crudContabilidade.controller;

import com.joazao.crudContabilidade.dto.*;
import com.joazao.crudContabilidade.model.Cliente;
import com.joazao.crudContabilidade.model.Produto;
import com.joazao.crudContabilidade.repository.ClienteRepository;
import com.joazao.crudContabilidade.repository.ProdutoRepository;
import com.joazao.crudContabilidade.service.VendaService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/vendas")
public class VendaController {
    private final VendaService vendaService;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public VendaController(VendaService vendaService,
                          ClienteRepository clienteRepository,
                          ProdutoRepository produtoRepository) {
        this.vendaService = vendaService;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public String listarVendas(Model model) {
        List<VendaDTO> vendas = vendaService.findAll();
        model.addAttribute("vendas", vendas);
        return "vendas/listar";
    }

    @GetMapping("/nova")
    public String novaVenda(Model model) {
        List<Cliente> clientes = clienteRepository.findAll();
        List<Produto> produtos = produtoRepository.findByEstoqueGreaterThan(0);
        model.addAttribute("clientes", clientes);
        model.addAttribute("produtos", produtos);
        model.addAttribute("venda", new VendaCreateDTO(null, "AVISTA", null, 0.0, 0.0, List.of()));
        return "vendas/nova";
    }

    @PostMapping("/nova")
    @Transactional
    public String salvarVenda(@RequestParam("clienteId") Long clienteId,
                             @RequestParam("produtoId") Long produtoId,
                             @RequestParam("quantidade") Integer quantidade,
                             @RequestParam("tipoVenda") String tipoVenda,
                             @RequestParam(value = "numeroParcelas", required = false) Integer numeroParcelas,
                             @RequestParam("valorTotal") double valorTotal,
                             @RequestParam("valorIcms") double valorIcms,
                             RedirectAttributes redirectAttributes) {
        try {
            // Buscar produto para obter o preço
            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            
            // Criar item da venda
            ItemVendaCreateDTO itemDTO = new ItemVendaCreateDTO(
                produtoId,
                quantidade,
                produto.getPrecoVenda()
            );
            
            // Criar DTO da venda
            VendaCreateDTO vendaDTO = new VendaCreateDTO(
                clienteId,
                tipoVenda,
                numeroParcelas,
                valorTotal,
                valorIcms,
                List.of(itemDTO)
            );
            
            vendaService.save(vendaDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Venda realizada com sucesso!");
            return "redirect:/vendas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao realizar venda: " + e.getMessage());
            return "redirect:/vendas/nova";
        }
    }

    @GetMapping("/{id}")
    public String detalharVenda(@PathVariable Long id, Model model) {
        VendaDTO venda = vendaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));
        
        // Calcular ICMS a pagar
        double icmsAPagar = vendaService.calcularIcmsAPagar(id);
        
        model.addAttribute("venda", venda);
        model.addAttribute("itens", venda.itens());
        model.addAttribute("icmsAPagar", icmsAPagar);
        return "vendas/detalhar";
    }
} 