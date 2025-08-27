package com.joazao.crudContabilidade.service;

import com.joazao.crudContabilidade.dto.ClienteDTO;
import com.joazao.crudContabilidade.model.Cliente;
import com.joazao.crudContabilidade.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<ClienteDTO> findById(Long id) {
        return clienteRepository.findById(id).map(this::toDTO);
    }

    public ClienteDTO save(ClienteDTO dto) {
        Cliente cliente = toEntity(dto);
        Cliente saved = clienteRepository.save(cliente);
        return toDTO(saved);
    }

    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

    private ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getCpf(),
                cliente.getNome(),
                cliente.getCidade(),
                cliente.getEstado()
        );
    }

    private Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.id());
        cliente.setCpf(dto.cpf());
        cliente.setNome(dto.nome());
        cliente.setCidade(dto.cidade());
        cliente.setEstado(dto.estado());
        return cliente;
    }
} 