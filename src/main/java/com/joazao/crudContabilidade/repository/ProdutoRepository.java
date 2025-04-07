package com.joazao.crudContabilidade.repository;


import com.joazao.crudContabilidade.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
