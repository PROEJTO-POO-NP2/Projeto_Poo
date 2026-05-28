package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.ResponsavelSaude;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para a entidade {@link Consulta}.
 *
 * Fornece operações CRUD padrão e métodos personalizados para
 * busca de consultas por paciente, data e profissional responsável.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
public interface ConsultaRepositorio extends JpaRepository<Consulta, Long> {

    /** Busca uma consulta de um paciente específico. */
    Optional<Consulta> findByPaciente(Paciente paciente);

    /** Busca todas as consultas de um paciente. */
    List<Consulta> findAllByPaciente(Paciente paciente);

    /** Busca consultas por data e profissional responsável. */
    List<Consulta> findByDataAndResponsavelSaude(LocalDate data, ResponsavelSaude responsavelSaude);

    /** Busca todas as consultas de uma data específica. */
    List<Consulta> findByData(LocalDate data);
}
