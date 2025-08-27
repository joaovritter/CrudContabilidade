package com.joazao.crudContabilidade.repository;

import com.joazao.crudContabilidade.model.ItemCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCompraRepository extends JpaRepository<ItemCompra, Long> {
} 