package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório JPA para a entidade {@link Prontuario}.
 *
 * Fornece operações CRUD padrão e métodos personalizados
 * para busca de prontuários por paciente ou CPF.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {

    /** Busca o prontuário de um paciente específico. */
    Optional<Prontuario> findByPaciente(Paciente paciente);

    /** Busca o prontuário pelo CPF do paciente. */
    Optional<Prontuario> findByPacienteCpf(String cpf);
}
