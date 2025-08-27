package com.joazao.crudContabilidade.controller;

import com.joazao.crudContabilidade.service.BalancoPatrimonialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequestMapping("/contabilidade/balanco")
public class BalancoPatrimonialController {
    
    private final BalancoPatrimonialService balancoService;
    
    public BalancoPatrimonialController(BalancoPatrimonialService balancoService) {
        this.balancoService = balancoService;
    }
    
    @GetMapping
    public String balancoPatrimonial(Model model) {
        // Ativos
        BigDecimal caixa = balancoService.getSaldoCaixa();
        BigDecimal estoques = balancoService.getValorEstoques();
        BigDecimal contasReceber = balancoService.getContasReceber();
        BigDecimal bensPatrimoniais = balancoService.getBensPatrimoniais();
        BigDecimal totalAtivo = balancoService.getTotalAtivo();
        
        // Passivos
        BigDecimal contasPagar = balancoService.getContasPagar();
        BigDecimal totalPassivo = balancoService.getTotalPassivo();
        
        // Patrimônio Líquido
        BigDecimal capitalSocial = balancoService.getCapitalSocial();
        BigDecimal lucrosPrejuizos = balancoService.getLucrosPrejuizosAcumulados();
        BigDecimal totalPatrimonioLiquido = balancoService.getTotalPatrimonioLiquido();
        
        // Adicionar atributos ao modelo
        model.addAttribute("caixa", caixa);
        model.addAttribute("estoques", estoques);
        model.addAttribute("contasReceber", contasReceber);
        model.addAttribute("bensPatrimoniais", bensPatrimoniais);
        model.addAttribute("totalAtivo", totalAtivo);
        
        model.addAttribute("contasPagar", contasPagar);
        model.addAttribute("totalPassivo", totalPassivo);
        
        model.addAttribute("capitalSocial", capitalSocial);
        model.addAttribute("lucrosPrejuizos", lucrosPrejuizos);
        model.addAttribute("totalPatrimonioLiquido", totalPatrimonioLiquido);
        
        return "contabilidade/balancoPatrimonial/index";
    }
} 