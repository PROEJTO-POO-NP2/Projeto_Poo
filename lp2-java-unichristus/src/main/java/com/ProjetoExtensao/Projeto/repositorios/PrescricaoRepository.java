package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Prescricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescricaoRepository extends JpaRepository<Prescricao, Long> {
}
