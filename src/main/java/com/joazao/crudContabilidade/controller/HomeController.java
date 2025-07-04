package com.joazao.crudContabilidade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/contabilidade")
    public String contabilidade() {
        return "contabilidade/index";
    }
} 