package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.ResponsavelSaude;
import com.ProjetoExtensao.Projeto.repositorios.ResponsavelRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pelas regras de negócio dos Responsáveis de Saúde.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Service
@AllArgsConstructor
public class ResponsavelService {
    private final ResponsavelRepositorio responsavelRepositorio;

    /**
     * Busca um responsável de saúde pelo e-mail (usado no processo de login).
     * @param email e-mail para busca
     * @return o responsável ou null se não encontrado
     */
    public ResponsavelSaude findResponsavelByEmail(String email) {
        return responsavelRepositorio.findByEmail(email).orElse(null);
    }

    /**
     * Lista todos os responsáveis de saúde cadastrados.
     * @return lista completa
     */
    public List<ResponsavelSaude> findAllResponsaveis() {
        return responsavelRepositorio.findAll();
    }

    /**
     * Busca um responsável de saúde pelo nome completo.
     * @param nome nome para busca
     * @return o responsável encontrado
     * @throws RuntimeException se não encontrar
     */
    public ResponsavelSaude findResponsavelByNome(String nome) {
        return responsavelRepositorio.findByNomeCompleto(nome).orElseThrow(() -> new RuntimeException("Médico não encontrado"));
    }
}
