package com.joazao.crudContabilidade.repository;

import com.joazao.crudContabilidade.model.Caixa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaixaRepository extends JpaRepository<Caixa, Long> {
    // Aqui você pode adicionar métodos personalizados de consulta, se necessário
}
