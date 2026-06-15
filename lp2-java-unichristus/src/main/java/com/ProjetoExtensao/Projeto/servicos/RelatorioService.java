package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Prescricao;
import com.ProjetoExtensao.Projeto.models.Prontuario;
import com.ProjetoExtensao.Projeto.models.Vacina;
import com.ProjetoExtensao.Projeto.repositorios.ProntuarioRepository;
import com.ProjetoExtensao.Projeto.repositorios.RelatorioRepository;
import com.ProjetoExtensao.Projeto.utils.EventosOcorridos;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela geração de relatórios e estatísticas clínicas do sistema.
 *
 * <p>Implementa quatro análises distintas exigidas pelo trabalho acadêmico:</p>
 * <ol>
 *   <li>Relatório Individual — consolida ficha pessoal, prescrições e vacinas de um residente</li>
 *   <li>Percentual de Vacinação — cobertura de um imunobiológico nos residentes ativos</li>
 *   <li>Percentual de Incidentes — proporção de residentes acometidos por eventos sentinelas</li>
 *   <li>Contagem por Tipo de Evento — agrupamento de ocorrências sentinelas por categoria</li>
 * </ol>
 *
 * <p><b>Dependências:</b></p>
 * <ul>
 *   <li>{@link RelatorioRepository} — consultas de pacientes por período</li>
 *   <li>{@link PacienteService} — listagem de residentes ativos</li>
 *   <li>{@link VacinaService} — delegação do cálculo de cobertura vacinal</li>
 *   <li>{@link EventoSentinelaService} — consulta de ocorrências adversas</li>
 *   <li>{@link ProntuarioRepository} — acesso às prescrições via prontuário</li>
 * </ul>
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Service
@AllArgsConstructor
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;
    private final PacienteService pacienteService;
    private final VacinaService vacinaService;
    private final EventoSentinelaService eventoSentinelaService;
    private final ProntuarioRepository prontuarioRepository;

    // =====================================================================
    // === Métodos de Relatório por Período (legado, mantido para compat.) ===
    // =====================================================================

    /**
     * Busca residentes cujo campo {@code dataEntrada} esteja dentro do intervalo especificado.
     *
     * @param inicio data de início do intervalo (inclusiva)
     * @param fim    data de fim do intervalo (inclusiva)
     * @return lista de pacientes que entraram na instituição dentro do período
     * @throws IllegalArgumentException se as datas forem nulas ou a data de início for posterior à de fim
     */
    public List<Paciente> gerarRelatorioPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("As datas de início e fim não podem ser nulas.");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim.");
        }
        return relatorioRepository.findByDataEntradaBetween(inicio, fim);
    }

    // =====================================================================
    // === Relatório Individual ============================================
    // =====================================================================

    /**
     * Gera o relatório clínico individual completo de um residente.
     *
     * <p>Busca o residente pelo CPF, localiza o seu prontuário para obter as prescrições
     * médicas e consulta separadamente o histórico de vacinas.
     * O resultado é encapsulado em um {@link RelatorioIndividualDTO}.</p>
     *
     * @param cpf CPF do residente (com ou sem máscara — dígitos são extraídos automaticamente)
     * @return DTO contendo dados pessoais, lista de prescrições e lista de vacinas
     * @throws RuntimeException se o paciente não for encontrado pelo CPF informado
     */
    public RelatorioIndividualDTO gerarRelatorioIndividual(String cpf) {
        // Remove caracteres de máscara (pontos, traços) deixando apenas dígitos
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        Paciente paciente = pacienteService.findPacienteByCpf(cpfLimpo);

        // Tenta localizar o prontuário; se não existir, usa lista vazia de prescrições
        Optional<Prontuario> prontuarioOpt = prontuarioRepository.findByPaciente(paciente);
        List<Prescricao> prescricoes = prontuarioOpt
                .map(Prontuario::getPrescricoes)
                .orElse(new ArrayList<>());

        List<Vacina> vacinas = vacinaService.findByPaciente(paciente);

        return new RelatorioIndividualDTO(paciente, prescricoes, vacinas);
    }

    // =====================================================================
    // === Estatísticas de Vacinação ======================================
    // =====================================================================

    /**
     * Calcula o percentual de residentes ativos vacinados com um imunobiológico específico.
     *
     * <p>Delega o cálculo ao {@link VacinaService#calcularPercentualVacinacao(String)},
     * que aplica a fórmula: {@code (vacinados_ativos / total_ativos) * 100}.</p>
     *
     * @param nomeVacina nome da vacina a ser analisada (busca case-insensitive)
     * @return percentual de cobertura entre 0.0 e 100.0
     */
    public double calcularPercentualVacinacao(String nomeVacina) {
        return vacinaService.calcularPercentualVacinacao(nomeVacina);
    }

    // =====================================================================
    // === Estatísticas de Incidentes =====================================
    // =====================================================================

    /**
     * Calcula o percentual de residentes ativos que sofreram pelo menos um evento sentinela.
     *
     * <p>Percorre todos os pacientes ativos e verifica se cada um possui ao menos um
     * evento sentinela registrado, contabilizando a proporção sobre o total de ativos.</p>
     *
     * <p>Fórmula: {@code (ativos_com_incidente / total_ativos) * 100}</p>
     *
     * @return percentual entre 0.0 e 100.0; retorna 0.0 se não houver residentes ativos
     */
    public double calcularPercentualIncidentes() {
        List<Paciente> pacientesAtivos = pacienteService.findPacientesByAtivo(true);
        if (pacientesAtivos.isEmpty()) {
            return 0.0;
        }
        long pacientesComIncidente = pacientesAtivos.stream()
                .filter(p -> !eventoSentinelaService.findEventosByPaciente(p).isEmpty())
                .count();
        return ((double) pacientesComIncidente / pacientesAtivos.size()) * 100.0;
    }

    /**
     * Agrupa e conta todos os eventos sentinelas registrados por tipo de ocorrência.
     *
     * <p>Retorna um mapa onde a chave é o enum {@link EventosOcorridos} e o valor
     * é a quantidade total de ocorrências daquele tipo em todo o histórico do sistema.</p>
     *
     * <p>Utilizado pela aba "Percentual de Incidentes" da {@code TelaRelatorios}
     * para exibir o quadro resumo de ocorrências.</p>
     *
     * @return mapa {@code <TipoEvento, Quantidade>} agrupado por tipo de evento sentinela
     */
    public Map<EventosOcorridos, Long> contarEventosPorTipo() {
        return eventoSentinelaService.findAllEventos().stream()
                .collect(Collectors.groupingBy(
                        EventoSentinela::getEventosOcorridos,
                        Collectors.counting()
                ));
    }
}