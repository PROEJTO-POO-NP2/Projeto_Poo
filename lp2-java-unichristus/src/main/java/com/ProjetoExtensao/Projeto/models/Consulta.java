package com.ProjetoExtensao.Projeto.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entidade que representa uma Consulta médica no sistema.
 *
 * Contém informações sobre data, hora, tipo, profissional responsável,
 * paciente, motivo, diagnóstico (com CID-10) e encaminhamentos.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Entity
@Table(name = "consultas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Consulta {

    /** Identificador único da consulta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Data da consulta. */
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    /** Horário da consulta. */
    @Column(nullable = false, columnDefinition = "TIME")
    private LocalTime hora;

    /** Tipo da consulta (ROTINA, EMERGENCIAL, ESPECIALIZADA). */
    @Enumerated(EnumType.STRING)
    private TipoConsulta tipoConsulta;

    /** Profissional de saúde responsável pela consulta. */
    @ManyToOne
    @JoinColumn(name = "responsavel_id", nullable = false)
    private ResponsavelSaude responsavelSaude;

    /** Paciente atendido nesta consulta. */
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    /**
     * Prontuário ao qual esta consulta está vinculada.
     * Pode ser nulo caso o prontuário ainda não tenha sido criado.
     */
    @ManyToOne
    @JoinColumn(name = "prontuario_id")
    private Prontuario prontuario;

    /** Motivo/queixa principal que levou o paciente à consulta. */
    @Column(columnDefinition = "TEXT")
    private String motivoConsulta;

    /** Diagnóstico realizado pelo profissional (texto livre). */
    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    /** Código CID-10 do diagnóstico, se aplicável. */
    @Column(length = 10)
    private String codigoCID;

    /** Anotações livres do profissional sobre a consulta. */
    @Column(columnDefinition = "TEXT")
    private String anotacoesMedico;

    /**
     * Encaminhamento para exame ou especialista, se necessário.
     * Pode ser nulo quando não há encaminhamento.
     */
    @Column(columnDefinition = "TEXT")
    private String encaminhamento;

    /**
     * Construtor simplificado para criação rápida de consulta (usado no seed).
     *
     * @param data             data da consulta
     * @param hora             horário da consulta
     * @param tipoConsulta     tipo da consulta (String convertido para enum)
     * @param responsavelSaude profissional responsável
     * @param paciente         paciente atendido
     */
    public Consulta(LocalDate data, LocalTime hora, String tipoConsulta,
                    ResponsavelSaude responsavelSaude, Paciente paciente) {
        this.data = data;
        this.hora = hora;
        this.tipoConsulta = TipoConsulta.getType(tipoConsulta);
        this.responsavelSaude = responsavelSaude;
        this.paciente = paciente;
    }

    // ========== Métodos exigidos pela especificação ==========

    /**
     * Registra o diagnóstico da consulta com código CID-10 opcional.
     *
     * @param diagnostico texto descritivo do diagnóstico
     * @param codigoCID   código CID-10 (pode ser nulo ou vazio)
     */
    public void registrarDiagnostico(String diagnostico, String codigoCID) {
        this.diagnostico = diagnostico;
        this.codigoCID = (codigoCID != null && !codigoCID.isBlank()) ? codigoCID : null;
    }

    /**
     * Gera um encaminhamento para exame ou especialista.
     *
     * @param descricaoEncaminhamento descrição do encaminhamento
     */
    public void gerarEncaminhamento(String descricaoEncaminhamento) {
        this.encaminhamento = descricaoEncaminhamento;
    }
}
