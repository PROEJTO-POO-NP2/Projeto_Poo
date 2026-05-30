package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de CRUD da entidade {@link Paciente}.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
public interface PacienteRepositorio extends JpaRepository<Paciente, Long> {
    
    /**
     * Busca pacientes cujo nome contenha a string especificada, ignorando maiúsculas/minúsculas.
     * @param nome string a ser buscada no nome
     * @return lista de pacientes correspondentes
     */
    List<Paciente> findByNomeCompletoContainingIgnoreCase(String nome);

    /**
     * Busca pacientes cujo CPF contenha a string especificada.
     * @param cpf string a ser buscada no CPF
     * @return lista de pacientes correspondentes
     */
    List<Paciente> findByCpfContaining(String cpf);

    /**
     * Busca um paciente exato pelo seu CPF.
     * @param cpf CPF exato do paciente
     * @return Optional contendo o paciente, se encontrado
     */
    Optional<Paciente> findByCpf(String cpf);

    /**
     * Busca pacientes por status (ativo/inativo).
     * @param ativo true para buscar pacientes ativos, false para inativos
     * @return lista de pacientes com o status especificado
     */
    List<Paciente> findByAtivo(Boolean ativo);
}
