package com.ProjetoExtensao.Projeto.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entidade que representa o Prontuário Médico de um paciente.
 *
 * O prontuário centraliza todo o histórico clínico do paciente,
 * incluindo consultas realizadas, exames solicitados/resultados,
 * prescrições médicas e histórico de internações.
 *
 * Cada paciente possui um único prontuário (relacionamento 1:1).
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Entity
@Table(name = "prontuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Prontuario {

    /** Identificador único do prontuário, gerado automaticamente pelo banco. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Paciente ao qual este prontuário pertence (relacionamento 1:1). */
    @OneToOne
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    /** Data em que o prontuário foi criado no sistema. */
    @Column(nullable = false)
    private LocalDate dataCriacao;

    /** Observações gerais sobre o paciente (texto livre). */
    @Column(columnDefinition = "TEXT")
    private String observacoesGerais;

    /**
     * Lista de consultas vinculadas a este prontuário.
     * Relacionamento bidirecional com a entidade {@link Consulta}.
     */
    @OneToMany(mappedBy = "prontuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consulta> consultas = new ArrayList<>();

    /**
     * Lista de exames solicitados/realizados neste prontuário.
     * Relacionamento bidirecional com a entidade {@link Exame}.
     */
    @OneToMany(mappedBy = "prontuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exame> exames = new ArrayList<>();

    /**
     * Lista de prescrições médicas registradas neste prontuário.
     * Relacionamento bidirecional com a entidade {@link Prescricao}.
     */
    @OneToMany(mappedBy = "prontuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prescricao> prescricoes = new ArrayList<>();

    /**
     * Histórico de internações do paciente (lista de strings descritivas).
     * Armazenado em tabela auxiliar via {@code @ElementCollection}.
     */
    @ElementCollection
    @CollectionTable(name = "prontuario_internacoes", joinColumns = @JoinColumn(name = "prontuario_id"))
    @Column(name = "descricao_internacao", columnDefinition = "TEXT")
    private List<String> historicoInternacoes = new ArrayList<>();

    /**
     * Construtor para criação de um novo prontuário.
     *
     * @param paciente         paciente associado
     * @param dataCriacao      data de criação do prontuário
     * @param observacoesGerais observações gerais iniciais
     */
    public Prontuario(Paciente paciente, LocalDate dataCriacao, String observacoesGerais) {
        this.paciente = paciente;
        this.dataCriacao = dataCriacao;
        this.observacoesGerais = observacoesGerais;
    }

    // ========== Métodos exigidos pela especificação ==========

    /**
     * Adiciona uma nova consulta ao prontuário.
     *
     * @param consulta consulta a ser vinculada
     */
    public void adicionarConsulta(Consulta consulta) {
        consultas.add(consulta);
        consulta.setProntuario(this);
    }

    /**
     * Vincula o resultado de um exame ao prontuário.
     *
     * @param exame exame a ser adicionado
     */
    public void vincularExame(Exame exame) {
        exames.add(exame);
        exame.setProntuario(this);
    }

    /**
     * Remove um exame do prontuário.
     *
     * @param exame exame a ser removido
     */
    public void removeExame(Exame exame) {
        exames.remove(exame);
        exame.setProntuario(null);
    }

    /**
     * Adiciona uma prescrição médica ao prontuário.
     *
     * @param prescricao prescrição a ser adicionada
     */
    public void addPrescricao(Prescricao prescricao) {
        prescricoes.add(prescricao);
        prescricao.setProntuario(this);
    }

    /**
     * Remove uma prescrição do prontuário.
     *
     * @param prescricao prescrição a ser removida
     */
    public void removePrescricao(Prescricao prescricao) {
        prescricoes.remove(prescricao);
        prescricao.setProntuario(null);
    }

    /**
     * Adiciona um registro de internação ao histórico.
     *
     * @param descricao descrição da internação
     */
    public void adicionarInternacao(String descricao) {
        historicoInternacoes.add(descricao);
    }

    /**
     * Gera um resumo textual do histórico do paciente para uso em relatórios.
     *
     * @return string formatada com o resumo completo do prontuário
     */
    public String gerarResumo() {
        StringBuilder resumo = new StringBuilder();
        resumo.append("=== RESUMO DO PRONTUÁRIO ===\n");
        resumo.append("Paciente: ").append(paciente.getNomeCompleto()).append("\n");
        resumo.append("CPF: ").append(paciente.getCpf()).append("\n");
        resumo.append("Cartão SUS: ").append(paciente.getCartaoSUS()).append("\n");
        resumo.append("Data de Criação: ").append(dataCriacao).append("\n");
        resumo.append("Observações: ").append(observacoesGerais != null ? observacoesGerais : "Nenhuma").append("\n\n");

        resumo.append("--- Consultas (").append(consultas.size()).append(") ---\n");
        for (Consulta c : consultas) {
            resumo.append("  • ").append(c.getData()).append(" - ")
                    .append(c.getTipoConsulta()).append(" - Dr(a). ")
                    .append(c.getResponsavelSaude().getNomeCompleto()).append("\n");
        }

        resumo.append("\n--- Exames (").append(exames.size()).append(") ---\n");
        for (Exame ex : exames) {
            resumo.append("  • ").append(ex.getDataExame()).append(" - ")
                    .append(ex.getNomeExame()).append(" → ")
                    .append(ex.getResultado() != null ? ex.getResultado() : "Aguardando resultado").append("\n");
        }

        resumo.append("\n--- Prescrições (").append(prescricoes.size()).append(") ---\n");
        for (Prescricao p : prescricoes) {
            resumo.append("  • ").append(p.getMedicamento()).append(" - ")
                    .append(p.getDosagem()).append("\n");
        }

        resumo.append("\n--- Internações (").append(historicoInternacoes.size()).append(") ---\n");
        for (String internacao : historicoInternacoes) {
            resumo.append("  • ").append(internacao).append("\n");
        }

        return resumo.toString();
    }

    /**
     * Busca consultas do prontuário filtradas por data.
     *
     * @param data data a ser filtrada
     * @return lista de consultas na data especificada
     */
    public List<Consulta> buscarConsultasPorData(LocalDate data) {
        return consultas.stream()
                .filter(c -> c.getData().equals(data))
                .collect(Collectors.toList());
    }

    /**
     * Busca consultas do prontuário filtradas por profissional responsável.
     *
     * @param nomeProfissional nome do profissional
     * @return lista de consultas do profissional especificado
     */
    public List<Consulta> buscarConsultasPorProfissional(String nomeProfissional) {
        return consultas.stream()
                .filter(c -> c.getResponsavelSaude().getNomeCompleto()
                        .toLowerCase().contains(nomeProfissional.toLowerCase()))
                .collect(Collectors.toList());
    }
}
