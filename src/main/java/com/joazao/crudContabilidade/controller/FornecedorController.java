package com.joazao.crudContabilidade.controller;

import com.joazao.crudContabilidade.model.Fornecedor;
import com.joazao.crudContabilidade.model.Produto;
import com.joazao.crudContabilidade.repository.FornecedorRepository;
import com.joazao.crudContabilidade.repository.ProdutoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FornecedorController {
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;

    public FornecedorController(FornecedorRepository fornecedorRepository, ProdutoRepository produtoRepository) {
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/fornecedores")
    public String redirecionarParaSelecionar() {
        return "redirect:/fornecedores/selecionar";
    }

    @GetMapping("/fornecedores/selecionar")
    public String selecionarFornecedor(Model model) {
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();
        model.addAttribute("fornecedores", fornecedores);
        return "selecionarFornecedor";
    }

    @PostMapping("/fornecedores/cadastrar")
    @Transactional
    public String cadastrarFornecedor(@ModelAttribute Fornecedor fornecedor, RedirectAttributes redirectAttributes) {
        try {
            // Validar CNPJ
            if (fornecedor.getCnpj() == null || fornecedor.getCnpj().length() != 14) {
                redirectAttributes.addFlashAttribute("mensagemErro", "CNPJ deve ter 14 dígitos.");
                return "redirect:/fornecedores/selecionar";
            }

            // Verificar se CNPJ já existe
            if (fornecedorRepository.findByCnpj(fornecedor.getCnpj()) != null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "CNPJ já cadastrado.");
                return "redirect:/fornecedores/selecionar";
            }

            fornecedorRepository.save(fornecedor);
            redirectAttributes.addFlashAttribute("mensagem", "Fornecedor cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar fornecedor: " + e.getMessage());
        }
        return "redirect:/fornecedores/selecionar";
    }

    @GetMapping("/fornecedores/{id}/produtos")
    public String listarProdutos(@PathVariable Long id, Model model) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElse(null);
        if (fornecedor == null) {
            return "redirect:/fornecedores/selecionar";
        }
        List<Produto> produtos = produtoRepository.findByFornecedorId(id);
        model.addAttribute("fornecedor", fornecedor);
        model.addAttribute("produtos", produtos);
        return "produtosFornecedor";
    }

    @GetMapping("/fornecedores/{id}/cadastrar-produto")
    public String cadastrarProduto(@PathVariable Long id, Model model) {
        Fornecedor fornecedor = fornecedorRepository.findById(id).orElse(null);
        if (fornecedor == null) {
            return "redirect:/fornecedores/selecionar";
        }
        model.addAttribute("fornecedor", fornecedor);
        model.addAttribute("novoProduto", new Produto());
        return "cadastrarProduto";
    }

    @PostMapping("/fornecedores/{id}/cadastrar-produto")
    @Transactional
    public String salvarProduto(
            @PathVariable Long id,
            @ModelAttribute Produto produto,
            RedirectAttributes redirectAttributes) {
        
        try {
            Fornecedor fornecedor = fornecedorRepository.findById(id).orElse(null);
            if (fornecedor == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Fornecedor não encontrado.");
                return "redirect:/fornecedores/selecionar";
            }

            // Criar uma nova instância de Produto para evitar problemas de estado
            Produto novoProduto = new Produto();
            novoProduto.setNome(produto.getNome());
            novoProduto.setPrecoCompra(produto.getPrecoCompra());
            novoProduto.setPrecoVenda(produto.getPrecoVenda());
            novoProduto.setIcms(produto.getIcms());
            novoProduto.setDebito(produto.getDebito());
            novoProduto.setCredito(produto.getCredito());
            novoProduto.setFornecedor(fornecedor);

            produtoRepository.save(novoProduto);
            redirectAttributes.addFlashAttribute("mensagem", "Produto cadastrado com sucesso!");
            return "redirect:/fornecedores/" + id + "/produtos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar produto: " + e.getMessage());
            return "redirect:/fornecedores/" + id + "/cadastrar-produto";
        }
    }
}