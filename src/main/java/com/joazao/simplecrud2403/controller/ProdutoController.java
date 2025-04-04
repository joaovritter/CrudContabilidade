package com.joazao.simplecrud2403.controller;

import com.joazao.simplecrud2403.model.Produto;
import com.joazao.simplecrud2403.repository.ProdutoRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller //para retornar uma pagina html
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("produto", new Produto());//guardando o objeto produto dentro do model
        return "form";  //thymeleaf entende que essa string é um formulario html
    }

    @PostMapping
    public String salvar(@ModelAttribute Produto produto) {
        System.out.println(produto.toString());

        produtoRepository.save(produto);
        return "redirect:/produto/novo"; //limpa a tela e ja redireciona para o /novo
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", produtoRepository.findAll());
        return "lista";
    }

    @GetMapping("/editar/{id}")
    public String formEditar(@PathVariable Long id, Model model) {
        Produto produto = produtoRepository.findById(id).get();
        model.addAttribute("produto", produto);//crio um objeto no model a partir do id e
        return "redirect:/produto/novo"; //joga para o formulario
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        produtoRepository.deleteById(id);
        return "redirect:/produto";
    }
    //ta

}
