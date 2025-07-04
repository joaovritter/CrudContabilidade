package com.joazao.crudContabilidade.repository;

import com.joazao.crudContabilidade.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByFornecedorId(Long fornecedorId);
    List<Produto> findByEstoqueGreaterThan(int estoque);
    List<Produto> findByFornecedorIdAndEstoqueGreaterThan(Long fornecedorId, int estoque);
}
