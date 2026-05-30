package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Prescricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para operações de CRUD da entidade Prescricao.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Repository
public interface PrescricaoRepository extends JpaRepository<Prescricao, Long> {
}
