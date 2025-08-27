package com.joazao.crudContabilidade.controller;

import com.joazao.crudContabilidade.dto.PatrimonioDTO;
import com.joazao.crudContabilidade.dto.PatrimonioCreateDTO;
import com.joazao.crudContabilidade.service.PatrimonioService;
import com.joazao.crudContabilidade.repository.FornecedorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/patrimonios")
public class PatrimonioController {
    private final PatrimonioService patrimonioService;
    private final FornecedorRepository fornecedorRepository;


    public PatrimonioController(PatrimonioService patrimonioService, FornecedorRepository fornecedorRepository) {
        this.patrimonioService = patrimonioService;
        this.fornecedorRepository = fornecedorRepository;
    }

    @GetMapping
    public String listarPatrimonios(Model model) {
        List<PatrimonioDTO> patrimonios = patrimonioService.findAll();
        model.addAttribute("patrimonios", patrimonios);
        return "patrimonios/listar";
    }

    @GetMapping("/novo")
    public String novoPatrimonio(Model model) {
        model.addAttribute("patrimonio", new PatrimonioCreateDTO("", "", null, null, null, null, null, null, null, null, null));
        model.addAttribute("fornecedores", fornecedorRepository.findAllByOrderByNomeAsc());
        return "patrimonios/novo";
    }

    @PostMapping("/novo")
    public String salvarPatrimonio(@ModelAttribute PatrimonioCreateDTO patrimonioDTO, RedirectAttributes redirectAttributes) {
        try {
            patrimonioService.save(patrimonioDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Patrimônio cadastrado com sucesso!");
            return "redirect:/patrimonios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar patrimônio: " + e.getMessage());
            return "redirect:/patrimonios/novo";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarPatrimonio(@PathVariable Long id, Model model) {
        PatrimonioDTO patrimonio = patrimonioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Patrimônio não encontrado"));
        model.addAttribute("patrimonio", patrimonio);
        model.addAttribute("fornecedores", fornecedorRepository.findAllByOrderByNomeAsc());
        return "patrimonios/editar";
    }

    @PostMapping("/{id}/editar")
    public String atualizarPatrimonio(@PathVariable Long id, @ModelAttribute PatrimonioCreateDTO patrimonioDTO, RedirectAttributes redirectAttributes) {
        try {
            patrimonioService.update(id, patrimonioDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Patrimônio atualizado com sucesso!");
            return "redirect:/patrimonios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar patrimônio: " + e.getMessage());
            return "redirect:/patrimonios/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluirPatrimonio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            patrimonioService.delete(id);
            redirectAttributes.addFlashAttribute("mensagem", "Patrimônio excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir patrimônio: " + e.getMessage());
        }
        return "redirect:/patrimonios";
    }
} 