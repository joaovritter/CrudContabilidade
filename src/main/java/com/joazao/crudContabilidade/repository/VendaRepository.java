package com.joazao.crudContabilidade.repository;

import com.joazao.crudContabilidade.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByClienteId(Long clienteId);
    List<Venda> findAllByOrderByDataVendaDesc();
} 