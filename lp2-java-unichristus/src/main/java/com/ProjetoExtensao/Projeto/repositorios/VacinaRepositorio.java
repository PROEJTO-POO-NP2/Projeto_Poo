package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório JPA para operações de acesso a dados da entidade {@link Vacina}.
 *
 * <p>Estende {@link JpaRepository} fornecendo as operações padrão de CRUD e
 * métodos derivados para consultas específicas do módulo de controle vacinal.</p>
 *
 * <p>Os métodos de consulta utilizam a convenção de nomenclatura do Spring Data JPA
 * para geração automática de queries SQL em tempo de execução.</p>
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Repository
public interface VacinaRepositorio extends JpaRepository<Vacina, Long> {

    /**
     * Busca todas as vacinas registradas para um determinado paciente.
     *
     * @param paciente o paciente cujas vacinas se deseja consultar
     * @return lista de vacinas do paciente (pode ser vazia)
     */
    List<Vacina> findByPaciente(Paciente paciente);

    /**
     * Busca vacinas pelo nome exato, ignorando diferenças de maiúsculas/minúsculas.
     *
     * <p>Útil para consolidar registros do mesmo imunobiológico escritos de formas
     * diferentes (ex: "gripe", "Gripe", "GRIPE").</p>
     *
     * @param nomeVacina nome da vacina a ser pesquisada
     * @return lista de vacinas com o nome correspondente
     */
    List<Vacina> findByNomeVacinaIgnoreCase(String nomeVacina);

    /**
     * Conta quantas aplicações de uma vacina específica foram feitas em pacientes ativos.
     *
     * <p>Este método é utilizado pelo {@code VacinaService} para calcular o
     * percentual de cobertura vacinal dos residentes ativos da instituição.</p>
     *
     * @param nomeVacina nome da vacina (busca case-insensitive)
     * @param ativo      status do paciente (true = ativo, false = inativo)
     * @return número de registros de vacinação encontrados
     */
    long countByNomeVacinaIgnoreCaseAndPacienteAtivo(String nomeVacina, Boolean ativo);

    /**
     * Busca todas as vacinas de um paciente ordenadas da mais recente para a mais antiga.
     *
     * <p>Utilizado na listagem principal do módulo de Vacinas para exibir o histórico
     * vacinal do residente em ordem cronológica decrescente.</p>
     *
     * @param paciente o paciente cujo histórico se deseja consultar
     * @return lista de vacinas ordenadas por data de aplicação descendente
     */
    List<Vacina> findByPacienteOrderByDataAplicacaoDesc(Paciente paciente);
}
