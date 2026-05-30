package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.infra.FormatadorDataHora;
import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.TipoConsulta;
import com.ProjetoExtensao.Projeto.repositorios.ConsultaRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Serviço responsável pela lógica de negócios das Consultas.
 *
 * Gerencia operações de busca, criação e atualização de consultas,
 * incluindo registro de diagnósticos e geração de encaminhamentos.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Service
@AllArgsConstructor
public class ConsultaService {

    private ConsultaRepositorio consultaRepositorio;
    private ResponsavelService responsavelService;
    private PacienteService pacienteService;

    /**
     * Busca uma consulta de um paciente específico.
     *
     * @param paciente paciente a ser buscado
     * @return consulta encontrada
     * @throws RuntimeException se nenhuma consulta for encontrada
     */
    public Consulta findConsultaByPaciente(Paciente paciente) {
        return consultaRepositorio.findByPaciente(paciente)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    }

    /**
     * Busca todas as consultas de um paciente.
     *
     * @param paciente paciente cujas consultas serão buscadas
     * @return lista de consultas do paciente
     */
    public List<Consulta> findAllConsultasByPaciente(Paciente paciente) {
        return consultaRepositorio.findAllByPaciente(paciente);
    }

    /**
     * Busca uma consulta pelo seu ID.
     *
     * @param id identificador da consulta
     * @return consulta encontrada
     * @throws RuntimeException se a consulta não for encontrada
     */
    public Consulta findConsultaById(Long id) {
        return consultaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
    }

    /**
     * Salva uma nova consulta no sistema com todos os campos.
     *
     * @param pacienteCpf    CPF do paciente
     * @param data           data da consulta (formato dd/MM/yyyy)
     * @param hora           hora da consulta (formato HH:mm)
     * @param medicoNome     nome do médico responsável
     * @param tipoConsulta   tipo da consulta (ROTINA, EMERGENCIAL, ESPECIALIZADA)
     * @param motivoConsulta motivo da consulta
     * @param diagnostico    diagnóstico (pode ser vazio)
     * @param anotacoesMedico anotações do médico (pode ser vazio)
     */
    public void salvarConsulta(String pacienteCpf, String data, String hora,
                               String medicoNome, String tipoConsulta,
                               String motivoConsulta, String diagnostico,
                               String anotacoesMedico) {
        Consulta consulta = new Consulta();

        consulta.setData(LocalDate.parse(data, FormatadorDataHora.DATE_TIME_FORMATTER));
        consulta.setHora(LocalTime.parse(hora, FormatadorDataHora.TIME_FORMATTER));
        consulta.setTipoConsulta(TipoConsulta.getType(tipoConsulta));

        consulta.setPaciente(pacienteService.findPacienteByCpf(pacienteCpf));
        consulta.setResponsavelSaude(responsavelService.findResponsavelByNome(medicoNome));

        consulta.setMotivoConsulta(motivoConsulta);
        consulta.setDiagnostico(diagnostico.isEmpty() ? null : diagnostico);
        consulta.setAnotacoesMedico(anotacoesMedico.isEmpty() ? null : anotacoesMedico);

        consultaRepositorio.save(consulta);
    }

    /**
     * Salva uma nova consulta com os campos adicionais de CID-10 e encaminhamento.
     *
     * @param pacienteCpf     CPF do paciente
     * @param data            data da consulta (formato dd/MM/yyyy)
     * @param hora            hora da consulta (formato HH:mm)
     * @param medicoNome      nome do médico responsável
     * @param tipoConsulta    tipo da consulta
     * @param motivoConsulta  motivo da consulta
     * @param diagnostico     diagnóstico
     * @param codigoCID       código CID-10
     * @param anotacoesMedico anotações do médico
     * @param encaminhamento  encaminhamento para exame/especialista
     */
    public void salvarConsultaCompleta(String pacienteCpf, String data, String hora,
                                       String medicoNome, String tipoConsulta,
                                       String motivoConsulta, String diagnostico,
                                       String codigoCID, String anotacoesMedico,
                                       String encaminhamento) {
        Consulta consulta = new Consulta();

        consulta.setData(LocalDate.parse(data, FormatadorDataHora.DATE_TIME_FORMATTER));
        consulta.setHora(LocalTime.parse(hora, FormatadorDataHora.TIME_FORMATTER));
        consulta.setTipoConsulta(TipoConsulta.getType(tipoConsulta));

        consulta.setPaciente(pacienteService.findPacienteByCpf(pacienteCpf));
        consulta.setResponsavelSaude(responsavelService.findResponsavelByNome(medicoNome));

        consulta.setMotivoConsulta(motivoConsulta);
        consulta.setDiagnostico(diagnostico != null && !diagnostico.isEmpty() ? diagnostico : null);
        consulta.setCodigoCID(codigoCID != null && !codigoCID.isEmpty() ? codigoCID : null);
        consulta.setAnotacoesMedico(anotacoesMedico != null && !anotacoesMedico.isEmpty() ? anotacoesMedico : null);
        consulta.setEncaminhamento(encaminhamento != null && !encaminhamento.isEmpty() ? encaminhamento : null);

        consultaRepositorio.save(consulta);
    }

    /**
     * Registra um diagnóstico em uma consulta existente.
     *
     * @param consultaId  ID da consulta
     * @param diagnostico texto do diagnóstico
     * @param codigoCID   código CID-10 (opcional)
     */
    public void registrarDiagnostico(Long consultaId, String diagnostico, String codigoCID) {
        Consulta consulta = findConsultaById(consultaId);
        consulta.registrarDiagnostico(diagnostico, codigoCID);
        consultaRepositorio.save(consulta);
    }

    /**
     * Gera um encaminhamento para exame ou especialista em uma consulta existente.
     *
     * @param consultaId         ID da consulta
     * @param encaminhamento     descrição do encaminhamento
     */
    public void gerarEncaminhamento(Long consultaId, String encaminhamento) {
        Consulta consulta = findConsultaById(consultaId);
        consulta.gerarEncaminhamento(encaminhamento);
        consultaRepositorio.save(consulta);
    }
}
