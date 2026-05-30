package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.ResponsavelSaude;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório para operações de CRUD da entidade {@link ResponsavelSaude}.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
public interface ResponsavelRepositorio extends JpaRepository<ResponsavelSaude, Long> {
    
    /**
     * Busca um profissional de saúde pelo seu e-mail (usado no login).
     * @param email e-mail do profissional
     * @return Optional contendo o responsável, se encontrado
     */
    Optional<ResponsavelSaude> findByEmail(String email);

    /**
     * Busca um profissional de saúde pelo nome completo.
     * @param nome nome completo do profissional
     * @return Optional contendo o responsável, se encontrado
     */
    Optional<ResponsavelSaude> findByNomeCompleto(String nome);
}
