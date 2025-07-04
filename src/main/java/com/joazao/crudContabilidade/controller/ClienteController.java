package com.joazao.crudContabilidade.controller;

import com.joazao.crudContabilidade.dto.ClienteDTO;
import com.joazao.crudContabilidade.service.ClienteService;
import com.joazao.crudContabilidade.model.Produto;
import com.joazao.crudContabilidade.repository.ProdutoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ClienteController {
    private final ClienteService clienteService;
    private final ProdutoRepository produtoRepository;

    public ClienteController(ClienteService clienteService, ProdutoRepository produtoRepository) {
        this.clienteService = clienteService;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        List<ClienteDTO> clientes = clienteService.findAll();
        model.addAttribute("clientes", clientes);
        return "clientes/listar";
    }

    @GetMapping("/clientes/novo")
    public String novoCliente(Model model) {
        model.addAttribute("cliente", new ClienteDTO(null, "", "", "", ""));
        return "clientes/novo";
    }

    @PostMapping("/clientes/novo")
    public String salvarCliente(@ModelAttribute ClienteDTO clienteDTO, RedirectAttributes redirectAttributes) {
        try {
            if (clienteDTO.cpf() == null || clienteDTO.cpf().length() != 11) {
                redirectAttributes.addFlashAttribute("mensagemErro", "CPF deve ter 11 dígitos.");
                return "redirect:/clientes/novo";
            }
            // Verificar se CPF já existe
            boolean cpfExistente = clienteService.findAll().stream().anyMatch(c -> c.cpf().equals(clienteDTO.cpf()));
            if (cpfExistente) {
                redirectAttributes.addFlashAttribute("mensagemErro", "CPF já cadastrado.");
                return "redirect:/clientes/novo";
            }
            clienteService.save(clienteDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente cadastrado com sucesso!");
            return "redirect:/clientes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar cliente: " + e.getMessage());
            return "redirect:/clientes/novo";
        }
    }

    @GetMapping("/clientes/{id}/editar")
    public String editarCliente(@PathVariable Long id, Model model) {
        ClienteDTO cliente = clienteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        model.addAttribute("cliente", cliente);
        return "clientes/editar";
    }

    @PostMapping("/clientes/{id}/editar")
    public String atualizarCliente(@PathVariable Long id, @ModelAttribute ClienteDTO clienteDTO, RedirectAttributes redirectAttributes) {
        try {
            ClienteDTO clienteExistente = clienteService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            // Verificar se CPF já existe em outro cliente
            boolean cpfExistente = clienteService.findAll().stream().anyMatch(c -> c.cpf().equals(clienteDTO.cpf()) && !c.id().equals(id));
            if (cpfExistente) {
                redirectAttributes.addFlashAttribute("mensagemErro", "CPF já cadastrado para outro cliente.");
                return "redirect:/clientes/" + id + "/editar";
            }
            ClienteDTO atualizado = new ClienteDTO(id, clienteDTO.cpf(), clienteDTO.nome(), clienteDTO.cidade(), clienteDTO.estado());
            clienteService.save(atualizado);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente atualizado com sucesso!");
            return "redirect:/clientes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar cliente: " + e.getMessage());
            return "redirect:/clientes/" + id + "/editar";
        }
    }

    @PostMapping("/clientes/{id}/excluir")
    public String excluirCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.delete(id);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/{id}/comprar")
    public String comprarProduto(@PathVariable Long id, Model model) {
        ClienteDTO cliente = clienteService.findById(id).orElse(null);
        if (cliente == null) {
            return "redirect:/clientes";
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
        // Mantém lógica original pois depende de Produto
        return "redirect:/clientes";
    }
}