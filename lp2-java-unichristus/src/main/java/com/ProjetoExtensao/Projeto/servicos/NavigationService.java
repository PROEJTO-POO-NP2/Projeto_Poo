package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.view.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pelo roteamento e navegação entre as telas do sistema.
 *
 * Utiliza o conceito de injeção de dependência "Lazy" para evitar
 * dependências circulares entre as telas e otimizar o carregamento.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Service
@NoArgsConstructor
public class NavigationService {
    @Lazy
    @Autowired
    private TelaLogin telaLogin;

    @Lazy
    @Autowired
    private TelaGeral telaGeral;

    @Lazy
    @Autowired
    private TelaPacientes telaPacientes;

    @Autowired
    @Lazy
    private TelaCadastroPacientes telaCadastroPacientes;

    @Lazy
    @Autowired
    private TelaConsultas consulta;

    @Autowired
    @Lazy
    private TelaAgendamentoConsulta telaAgendamentoConsulta;

    @Lazy
    @Autowired
    private TelaEventosSentinelas telaEventosSentinelas;

    @Lazy
    @Autowired
    private TelaProntuarios telaProntuarios;

    @Lazy
    @Autowired
    private TelaVacinas telaVacinas;

    @Lazy
    @Autowired
    private TelaRelatorios telaRelatorios;

    /** Abre a tela de Login e a torna visível. */
    public void abrirTelaLogin(){
        telaLogin.setVisible(true);
    }

    /** Abre o Painel Administrativo (Dashboard) e o torna visível. */
    public void abrirTelaGeral() {
        telaGeral.setVisible(true);
    }

    /** Abre a tela de listagem de Pacientes. */
    public void abrirTelaPacientes() {
        telaPacientes.setVisible(true);
    }

    /** Limpa os campos e abre a tela para cadastrar um novo Paciente. */
    public void abrirTelaCadastroPacientes() {
        telaCadastroPacientes.limparCamposAoAbrir();
        telaCadastroPacientes.setVisible(true);
    }

    /** Abre a tela de listagem de Consultas. */
    public void abrirTelaConsultas() {
        consulta.setVisible(true);
    }

    /** Abre a tela de Agendamento de novas Consultas. */
    public void abrirTelaAgendamentoConsultas() {
        telaAgendamentoConsulta.setVisible(true);
    }

    /**
     * Carrega os dados de um paciente e abre a tela de cadastro em modo de edição.
     * @param pacienteId ID do paciente a ser editado
     */
    public void abrirTelaEdicaoPaciente(Long pacienteId) {
        telaCadastroPacientes.carregarPacienteParaEdicao(pacienteId);
        telaCadastroPacientes.setVisible(true);
    }

    /** Limpa os campos e abre a tela de listagem de Eventos Sentinelas. */
    public void abrirTelaEventosSentinelas() {
        telaEventosSentinelas.limparCampos();
        telaEventosSentinelas.setVisible(true);
    }

    /** Abre a tela de Prontuários Médicos. */
    public void abrirTelaProntuarios() {
        telaProntuarios.setVisible(true);
    }

    /** Abre a tela de Controle de Vacinas. */
    public void abrirTelaVacinas() {
        telaVacinas.setVisible(true);
    }

    /** Abre a tela de Relatórios e Estatísticas. */
    public void abrirTelaRelatorios() {
        telaRelatorios.setVisible(true);
    }
}
