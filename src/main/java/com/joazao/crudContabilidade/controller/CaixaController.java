package com.joazao.crudContabilidade.controller;

import com.joazao.crudContabilidade.dto.CaixaDTO;
import com.joazao.crudContabilidade.dto.CaixaCreateDTO;
import com.joazao.crudContabilidade.model.enums.TipoMovimentacao;
import com.joazao.crudContabilidade.service.CaixaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/contabilidade/caixa")
public class CaixaController {
    private final CaixaService caixaService;

    public CaixaController(CaixaService caixaService) {
        this.caixaService = caixaService;
    }

    @GetMapping
    public String listar(Model model) {
        List<CaixaDTO> entradas = caixaService.findEntradasOrderByDataDesc();
        List<CaixaDTO> saidas = caixaService.findSaidasOrderByDataDesc();
        java.math.BigDecimal saldo = caixaService.getSaldo();
        model.addAttribute("entradas", entradas);
        model.addAttribute("saidas", saidas);
        model.addAttribute("saldo", saldo);
        return "contabilidade/caixa/listar";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("caixa", new CaixaCreateDTO(null, null, null, null, null, null));
        return "contabilidade/caixa/novo";
    }

    @PostMapping("/novo")
    public String salvar(@RequestParam("valor") java.math.BigDecimal valor,
                         @RequestParam("tipo") TipoMovimentacao tipo,
                         @RequestParam("descricao") String descricao,
                         RedirectAttributes redirectAttributes) {
        try {
            CaixaCreateDTO caixaDTO = new CaixaCreateDTO(
                java.time.LocalDate.now(),
                tipo,
                valor,
                descricao,
                null,
                java.time.LocalDateTime.now()
            );
            caixaService.save(caixaDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Movimentação registrada com sucesso!");
            return "redirect:/contabilidade/caixa";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao registrar movimentação: " + e.getMessage());
            return "redirect:/contabilidade/caixa/novo";
        }
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        CaixaDTO caixa = caixaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
        model.addAttribute("caixa", caixa);
        return "contabilidade/caixa/detalhar";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        CaixaDTO caixa = caixaService.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
        model.addAttribute("caixa", caixa);
        return "contabilidade/caixa/editar";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id, 
                           @RequestParam("valor") java.math.BigDecimal valor,
                           @RequestParam("tipo") TipoMovimentacao tipo,
                           @RequestParam("descricao") String descricao,
                           RedirectAttributes redirectAttributes) {
        try {
            CaixaDTO caixaExistente = caixaService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
            
            CaixaCreateDTO caixaDTO = new CaixaCreateDTO(
                caixaExistente.data(),
                tipo,
                valor,
                descricao,
                caixaExistente.relacionadoId(),
                caixaExistente.criadoEm()
            );
            
            caixaService.update(id, caixaDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Movimentação atualizada com sucesso!");
            return "redirect:/contabilidade/caixa/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar movimentação: " + e.getMessage());
            return "redirect:/contabilidade/caixa/" + id + "/editar";
        }
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            caixaService.delete(id);
            redirectAttributes.addFlashAttribute("mensagem", "Movimentação excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir movimentação: " + e.getMessage());
        }
        return "redirect:/contabilidade/caixa";
    }
} 