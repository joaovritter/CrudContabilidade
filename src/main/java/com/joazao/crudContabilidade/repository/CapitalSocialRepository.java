package com.joazao.crudContabilidade.repository;

import com.joazao.crudContabilidade.model.CapitalSocial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface CapitalSocialRepository extends JpaRepository<CapitalSocial, Long> {
} 