package com.joazao.crudContabilidade.controller;

import com.joazao.crudContabilidade.dto.CapitalSocialDTO;
import com.joazao.crudContabilidade.dto.CapitalSocialCreateDTO;
import com.joazao.crudContabilidade.service.CapitalSocialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/capitalsocial")
public class CapitalSocialController {
    private final CapitalSocialService capitalSocialService;

    public CapitalSocialController(CapitalSocialService capitalSocialService) {
        this.capitalSocialService = capitalSocialService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("capitais", capitalSocialService.findAll());
        return "contabilidade/capitalSocial/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("capitalSocial", new CapitalSocialCreateDTO(null, null));
        return "contabilidade/capitalSocial/novo";
    }

    @PostMapping("/novo")
    public String salvar(@ModelAttribute CapitalSocialCreateDTO capitalSocialDTO, RedirectAttributes redirectAttributes) {
        try {
            capitalSocialService.save(capitalSocialDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Capital social cadastrado com sucesso!");
            return "redirect:/capitalsocial";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar: " + e.getMessage());
            return "redirect:/capitalsocial/novo";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        CapitalSocialDTO capitalSocial = capitalSocialService.findById(id)
                .orElseThrow(() -> new RuntimeException("Capital social não encontrado"));
        model.addAttribute("capitalSocial", capitalSocial);
        return "contabilidade/capitalSocial/editar";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id, @ModelAttribute CapitalSocialCreateDTO capitalSocialDTO, RedirectAttributes redirectAttributes) {
        try {
            capitalSocialService.update(id, capitalSocialDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Capital social atualizado com sucesso!");
            return "redirect:/capitalsocial";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar: " + e.getMessage());
            return "redirect:/capitalsocial/" + id + "/editar";
        }
    }
} 