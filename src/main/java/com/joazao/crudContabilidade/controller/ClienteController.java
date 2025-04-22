package com.joazao.crudContabilidade.controller;

import com.joazao.crudContabilidade.model.Cliente;
import com.joazao.crudContabilidade.model.Produto;
import com.joazao.crudContabilidade.repository.ClienteRepository;
import com.joazao.crudContabilidade.repository.ProdutoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ClienteController {
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public ClienteController(ClienteRepository clienteRepository, ProdutoRepository produtoRepository) {
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/clientes")
    public String redirecionarParaSelecionar() {
        return "redirect:/clientes/selecionar";
    }

    @GetMapping("/clientes/selecionar")
    public String selecionarCliente(Model model) {
        List<Cliente> clientes = clienteRepository.findAll();
        model.addAttribute("clientes", clientes);
        return "selecionarCliente";
    }

    @PostMapping("/clientes/cadastrar")
    public String cadastrarCliente(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            // Validar CPF
            if (cliente.getCpf() == null || cliente.getCpf().length() != 11) {
                redirectAttributes.addFlashAttribute("mensagemErro", "CPF deve ter 11 dígitos.");
                return "redirect:/clientes/selecionar";
            }

            // Verificar se CPF já existe
            if (clienteRepository.findByCpf(cliente.getCpf()) != null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "CPF já cadastrado.");
                return "redirect:/clientes/selecionar";
            }

            clienteRepository.save(cliente);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar cliente: " + e.getMessage());
        }
        return "redirect:/clientes/selecionar";
    }

    @GetMapping("/clientes/{id}/comprar")
    public String comprarProduto(@PathVariable Long id, Model model) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente == null) {
            return "redirect:/clientes/selecionar";
        }
        List<Produto> produtos = produtoRepository.findAll();
        model.addAttribute("cliente", cliente);
        model.addAttribute("produtos", produtos);
        return "comprarProduto";
    }

    @PostMapping("/clientes/{id}/comprar")
    public String finalizarCompra(
            @PathVariable Long id,
            @RequestParam Long produtoId,
            @RequestParam String tipoCompra,
            @RequestParam(required = false, defaultValue = "1") int parcelas,
            RedirectAttributes redirectAttributes) {
        
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        Produto produto = produtoRepository.findById(produtoId).orElse(null);

        if (cliente == null || produto == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Cliente ou produto não encontrado.");
            return "redirect:/clientes/selecionar";
        }

        String mensagem;
        if ("avista".equals(tipoCompra)) {
            mensagem = produto.venderAVista();
        } else {
            if (parcelas < 1) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Número de parcelas inválido.");
                return "redirect:/clientes/" + id + "/comprar";
            }
            mensagem = produto.venderAPrazo(parcelas);
        }

        redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);
        return "redirect:/clientes/selecionar";
    }
}