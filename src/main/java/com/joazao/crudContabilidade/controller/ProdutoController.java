package com.joazao.crudContabilidade.controller;

import com.joazao.crudContabilidade.dto.ProdutoDTO;
import com.joazao.crudContabilidade.dto.ProdutoCreateDTO;
import com.joazao.crudContabilidade.service.ProdutoService;
import com.joazao.crudContabilidade.model.Fornecedor;
import com.joazao.crudContabilidade.repository.FornecedorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoService produtoService;

    public ProdutoController(FornecedorRepository fornecedorRepository, ProdutoService produtoService) {
        this.fornecedorRepository = fornecedorRepository;
        this.produtoService = produtoService;
    }

    @GetMapping("/cadastrar")
    public String exibirFormularioCadastroProduto(Model model) {
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();
        if (fornecedores.isEmpty()) {
            model.addAttribute("mensagemErro", "Nenhum fornecedor cadastrado. Cadastre um fornecedor antes de adicionar produtos.");
        } else {
            model.addAttribute("fornecedores", fornecedores);
        }
        model.addAttribute("produto", new ProdutoCreateDTO("", 0.0, 0.0, 0.0, 0.0, 0.0, null));
        return "produtos/cadastrarProduto";
    }

    @PostMapping("/cadastrar")
    public String cadastrarProduto(@ModelAttribute ProdutoCreateDTO produtoDTO, RedirectAttributes redirectAttributes) {
        try {
            produtoService.save(produtoDTO);
        redirectAttributes.addFlashAttribute("mensagem", "Produto cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar produto: " + e.getMessage());
        }
        return "redirect:/produtos/cadastrar";
    }

    @GetMapping("/{id}/editar")
    public String editarProduto(@PathVariable Long id, Model model) {
        ProdutoDTO produto = produtoService.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();
        model.addAttribute("produto", produto);
        model.addAttribute("fornecedores", fornecedores);
        return "produtos/editarProduto";
    }

    @PostMapping("/{id}/editar")
    public String atualizarProduto(@PathVariable Long id, @ModelAttribute ProdutoCreateDTO produtoDTO, RedirectAttributes redirectAttributes) {
        try {
            produtoService.update(id, produtoDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Produto atualizado com sucesso!");
            return "redirect:/produtos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar produto: " + e.getMessage());
            return "redirect:/produtos/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluirProduto(@PathVariable Long id, @RequestParam(value = "fornecedorId", required = false) Long fornecedorId, RedirectAttributes redirectAttributes) {
        try {
            produtoService.delete(id);
            redirectAttributes.addFlashAttribute("mensagem", "Produto excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir produto: " + e.getMessage());
        }
        if (fornecedorId != null) {
            return "redirect:/fornecedores/" + fornecedorId + "/produtos";
        }
        return "redirect:/produtos";
    }

    @GetMapping("")
    public String listarProdutos(Model model) {
        List<ProdutoDTO> produtos = produtoService.findAll();
        model.addAttribute("produtos", produtos);
        return "produtos/listarProdutos";
    }
}