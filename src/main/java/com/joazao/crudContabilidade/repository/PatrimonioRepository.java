package com.joazao.crudContabilidade.repository;

import com.joazao.crudContabilidade.model.Patrimonio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatrimonioRepository extends JpaRepository<Patrimonio, Long> {
    List<Patrimonio> findAllByOrderByDataAquisicaoDesc();
} 