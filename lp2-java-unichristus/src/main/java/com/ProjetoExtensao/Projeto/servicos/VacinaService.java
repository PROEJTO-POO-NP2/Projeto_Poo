package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Vacina;
import com.ProjetoExtensao.Projeto.repositorios.VacinaRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pelas regras de negócio do módulo de Controle de Vacinas.
 *
 * <p>Gerencia a persistência de registros vacinais dos residentes e fornece
 * métodos analíticos para cálculo de cobertura vacinal da instituição.
 * Utiliza injeção de dependência via construtor gerada pelo Lombok {@code @AllArgsConstructor}.</p>
 *
 * <p><b>Dependências:</b></p>
 * <ul>
 *   <li>{@link VacinaRepositorio} — acesso a dados da tabela de vacinas</li>
 *   <li>{@link PacienteService} — necessário para contar o total de pacientes ativos</li>
 * </ul>
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Service
@AllArgsConstructor
public class VacinaService {

    /** Repositório JPA para acesso às vacinas persistidas no banco. */
    private final VacinaRepositorio vacinaRepositorio;

    /** Serviço de pacientes, usado para calcular a base de residentes ativos. */
    private final PacienteService pacienteService;

    /**
     * Persiste uma nova vacina no banco de dados.
     *
     * @param vacina objeto {@link Vacina} devidamente preenchido para ser salvo
     */
    public void salvar(Vacina vacina) {
        vacinaRepositorio.save(vacina);
    }

    /**
     * Retorna todas as vacinas de um paciente (sem ordenação garantida).
     *
     * @param paciente o paciente cujas vacinas se deseja consultar
     * @return lista de vacinas associadas ao paciente
     */
    public List<Vacina> findByPaciente(Paciente paciente) {
        return vacinaRepositorio.findByPaciente(paciente);
    }

    /**
     * Retorna o histórico vacinal de um paciente ordenado do mais recente para o mais antigo.
     *
     * <p>Utilizado pela {@code TelaVacinas} para exibir a listagem principal de vacinas.</p>
     *
     * @param paciente o paciente cujo histórico se deseja consultar
     * @return lista de vacinas em ordem decrescente de data de aplicação
     */
    public List<Vacina> findByPacienteOrderByDataAplicacaoDesc(Paciente paciente) {
        return vacinaRepositorio.findByPacienteOrderByDataAplicacaoDesc(paciente);
    }

    /**
     * Retorna todas as vacinas registradas no sistema.
     *
     * @return lista completa de vacinas
     */
    public List<Vacina> findAll() {
        return vacinaRepositorio.findAll();
    }

    /**
     * Calcula o percentual de residentes ativos que tomaram uma vacina específica.
     *
     * <p>Fórmula: {@code (vacinados_ativos / total_ativos) * 100}</p>
     *
     * <p>Considera apenas pacientes com status {@code ativo = true} tanto no numerador
     * quanto no denominador para garantir consistência estatística.</p>
     *
     * @param nomeVacina nome da vacina (busca case-insensitive)
     * @return percentual de cobertura entre 0.0 e 100.0; retorna 0.0 se não houver pacientes ativos
     */
    public double calcularPercentualVacinacao(String nomeVacina) {
        long totalAtivos = pacienteService.findPacientesByAtivo(true).size();
        if (totalAtivos == 0) {
            return 0.0;
        }
        long vacinadosAtivos = vacinaRepositorio.countByNomeVacinaIgnoreCaseAndPacienteAtivo(nomeVacina, true);
        return ((double) vacinadosAtivos / totalAtivos) * 100.0;
    }
}
