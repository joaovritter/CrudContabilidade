package com.joazao.crudContabilidade.repository;

import com.joazao.crudContabilidade.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByCpf(String cpf);
    List<Cliente> findAllByOrderByNomeAsc();
}
