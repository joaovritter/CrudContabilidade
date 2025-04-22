package com.joazao.crudContabilidade.controller;

import com.joazao.crudContabilidade.model.Fornecedor;
import com.joazao.crudContabilidade.repository.FornecedorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.joazao.crudContabilidade.model.Produto;
import com.joazao.crudContabilidade.repository.ProdutoRepository;

@Controller
public class ProdutoController {

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;

    public ProdutoController(FornecedorRepository fornecedorRepository, ProdutoRepository produtoRepository) {
        this.fornecedorRepository = fornecedorRepository;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/produtos/cadastrar")
    public String exibirFormularioCadastroProduto(Model model) {
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();
        System.out.println("Fornecedores encontrados: " + fornecedores); // Log para verificar os dados
        if (fornecedores.isEmpty()) {
            model.addAttribute("mensagemErro", "Nenhum fornecedor cadastrado. Cadastre um fornecedor antes de adicionar produtos.");
        } else {
            model.addAttribute("fornecedores", fornecedores);
        }
        return "cadastrarProduto";
    }

    @PostMapping("/produtos/cadastrar")
    public String cadastrarProduto(String nome, Double preco, Long fornecedorId, RedirectAttributes redirectAttributes) {
        // Lógica para salvar o produto associado ao fornecedor
        redirectAttributes.addFlashAttribute("mensagem", "Produto cadastrado com sucesso!");
        return "redirect:/produtos/cadastrar";
    }

    @GetMapping("/produtos")
    public String listarProdutos(Model model) {
        List<Produto> produtos = produtoRepository.findAll();
        model.addAttribute("produtos", produtos);
        return "listarProdutos";
    }
}