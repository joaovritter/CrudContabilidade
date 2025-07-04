package com.joazao.crudContabilidade.repository;

import com.joazao.crudContabilidade.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Fornecedor findByCnpj(String cnpj);
    List<Fornecedor> findAllByOrderByNomeAsc();
}
