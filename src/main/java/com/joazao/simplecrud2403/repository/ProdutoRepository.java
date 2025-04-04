package com.joazao.simplecrud2403.repository;


import com.joazao.simplecrud2403.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
