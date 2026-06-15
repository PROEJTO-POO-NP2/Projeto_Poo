package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Prescricao;
import com.ProjetoExtensao.Projeto.models.Vacina;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * DTO (Data Transfer Object) que consolida os dados de um relatório individual de residente.
 *
 * <p>Agrupa em um único objeto as três dimensões de informação necessárias
 * para a geração do relatório clínico completo de um residente:</p>
 * <ol>
 *   <li>Dados cadastrais e pessoais do paciente</li>
 *   <li>Lista de prescrições médicas ativas registradas no prontuário</li>
 *   <li>Histórico de vacinas aplicadas</li>
 * </ol>
 *
 * <p>É populado pelo método {@code RelatorioService.gerarRelatorioIndividual(String cpf)}
 * e consumido pela aba "Relatório Individual" da {@code TelaRelatorios}.</p>
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 * @see RelatorioService
 */
@Getter
@AllArgsConstructor
public class RelatorioIndividualDTO {

    /** Dados cadastrais e pessoais do residente. */
    private Paciente paciente;

    /**
     * Lista de prescrições médicas registradas no prontuário do residente.
     * Pode ser vazia se nenhuma prescrição foi cadastrada.
     */
    private List<Prescricao> prescricoes;

    /**
     * Histórico completo de vacinas aplicadas no residente.
     * Pode ser vazia se nenhuma vacina foi registrada.
     */
    private List<Vacina> vacinas;
}
