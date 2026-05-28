package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Exame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {
}
