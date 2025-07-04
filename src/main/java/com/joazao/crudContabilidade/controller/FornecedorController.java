package com.joazao.crudContabilidade.controller;

import com.joazao.crudContabilidade.dto.FornecedorDTO;
import com.joazao.crudContabilidade.dto.FornecedorCreateDTO;
import com.joazao.crudContabilidade.service.FornecedorService;
import com.joazao.crudContabilidade.model.Produto;
import com.joazao.crudContabilidade.repository.ProdutoRepository;
import com.joazao.crudContabilidade.dto.ProdutoCreateDTO;
import com.joazao.crudContabilidade.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FornecedorController {
    private final FornecedorService fornecedorService;
    private final ProdutoRepository produtoRepository;
    private final ProdutoService produtoService;

    public FornecedorController(FornecedorService fornecedorService, ProdutoRepository produtoRepository, ProdutoService produtoService) {
        this.fornecedorService = fornecedorService;
        this.produtoRepository = produtoRepository;
        this.produtoService = produtoService;
    }

    @GetMapping("/fornecedores")
    public String listarFornecedores(Model model) {
        List<FornecedorDTO> fornecedores = fornecedorService.findAll();
        model.addAttribute("fornecedores", fornecedores);
        return "fornecedores/listar";
    }

    @GetMapping("/fornecedores/novo")
    public String novoFornecedor(Model model) {
        model.addAttribute("fornecedor", new FornecedorCreateDTO("", "", "", ""));
        return "fornecedores/novo";
    }

    @PostMapping("/fornecedores/novo")
    @Transactional
    public String cadastrarFornecedor(@ModelAttribute FornecedorCreateDTO fornecedorDTO, RedirectAttributes redirectAttributes) {
        try {
            if (fornecedorDTO.cnpj() == null || fornecedorDTO.cnpj().length() != 14) {
                redirectAttributes.addFlashAttribute("mensagemErro", "CNPJ deve ter 14 dígitos.");
                return "redirect:/fornecedores/novo";
            }
            boolean cnpjExistente = fornecedorService.findAll().stream().anyMatch(f -> f.cnpj().equals(fornecedorDTO.cnpj()));
            if (cnpjExistente) {
                redirectAttributes.addFlashAttribute("mensagemErro", "CNPJ já cadastrado.");
                return "redirect:/fornecedores/novo";
            }
            fornecedorService.save(fornecedorDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Fornecedor cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar fornecedor: " + e.getMessage());
        }
        return "redirect:/fornecedores";
    }

    @GetMapping("/fornecedores/{id}/editar")
    public String editarFornecedor(@PathVariable Long id, Model model) {
        FornecedorDTO fornecedor = fornecedorService.findById(id).orElse(null);
        if (fornecedor == null) {
            return "redirect:/fornecedores";
        }
        model.addAttribute("fornecedor", fornecedor);
        return "fornecedores/editar";
    }

    @PostMapping("/fornecedores/{id}/editar")
    @Transactional
    public String atualizarFornecedor(@PathVariable Long id, @ModelAttribute FornecedorCreateDTO fornecedorDTO, RedirectAttributes redirectAttributes) {
        try {
            FornecedorDTO fornecedorExistente = fornecedorService.findById(id).orElse(null);
            if (fornecedorExistente == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Fornecedor não encontrado.");
                return "redirect:/fornecedores";
            }
            if (fornecedorDTO.cnpj() == null || fornecedorDTO.cnpj().length() != 14) {
                redirectAttributes.addFlashAttribute("mensagemErro", "CNPJ deve ter 14 dígitos.");
                return "redirect:/fornecedores/" + id + "/editar";
            }
            boolean cnpjExistente = fornecedorService.findAll().stream().anyMatch(f -> f.cnpj().equals(fornecedorDTO.cnpj()) && !f.id().equals(id));
            if (cnpjExistente) {
                redirectAttributes.addFlashAttribute("mensagemErro", "CNPJ já cadastrado para outro fornecedor.");
                return "redirect:/fornecedores/" + id + "/editar";
            }
            fornecedorService.update(id, fornecedorDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Fornecedor atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar fornecedor: " + e.getMessage());
        }
        return "redirect:/fornecedores";
    }

    @PostMapping("/fornecedores/{id}/excluir")
    @Transactional
    public String excluirFornecedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            List<Produto> produtos = produtoRepository.findByFornecedorId(id);
            if (!produtos.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível excluir fornecedor que possui produtos cadastrados.");
                return "redirect:/fornecedores";
            }
            fornecedorService.delete(id);
            redirectAttributes.addFlashAttribute("mensagem", "Fornecedor excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir fornecedor: " + e.getMessage());
        }
        return "redirect:/fornecedores";
    }

    @GetMapping("/fornecedores/{id}/produtos")
    public String listarProdutos(@PathVariable Long id, Model model) {
        List<Produto> produtos = produtoRepository.findByFornecedorId(id);
        model.addAttribute("fornecedor", fornecedorService.findById(id).orElse(null));
        model.addAttribute("produtos", produtos);
        return "produtos/produtosFornecedor";
    }

    @GetMapping("/fornecedores/{id}/cadastrar-produto")
    public String cadastrarProduto(@PathVariable Long id, Model model) {
        model.addAttribute("fornecedor", fornecedorService.findById(id).orElse(null));
        model.addAttribute("novoProduto", new Produto());
        return "produtos/cadastrarProduto";
    }

    @PostMapping("/fornecedores/{id}/cadastrar-produto")
    @Transactional
    public String salvarProduto(
            @PathVariable Long id,
            @ModelAttribute ProdutoCreateDTO produtoDTO,
            RedirectAttributes redirectAttributes) {
        try {
            produtoService.save(produtoDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Produto cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar produto: " + e.getMessage());
        }
        return "redirect:/fornecedores/" + id + "/produtos";
    }
}