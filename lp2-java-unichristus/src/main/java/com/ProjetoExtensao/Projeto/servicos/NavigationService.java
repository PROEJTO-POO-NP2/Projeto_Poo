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

    public void abrirTelaLogin(){
        telaLogin.setVisible(true);
    }

    public void abrirTelaGeral() {
        telaGeral.setVisible(true);
    }

    public void abrirTelaPacientes() {
        telaPacientes.setVisible(true);
    }

    public void abrirTelaCadastroPacientes() {
        telaCadastroPacientes.limparCamposAoAbrir();
        telaCadastroPacientes.setVisible(true);
    }

    public void abrirTelaConsultas() {
        consulta.setVisible(true);
    }

    public void abrirTelaAgendamentoConsultas() {
        telaAgendamentoConsulta.setVisible(true);
    }

    public void abrirTelaEdicaoPaciente(Long pacienteId) {
        telaCadastroPacientes.carregarPacienteParaEdicao(pacienteId);
        telaCadastroPacientes.setVisible(true);
    }

    public void abrirTelaEventosSentinelas() {
        telaEventosSentinelas.limparCampos();
        telaEventosSentinelas.setVisible(true);
    }

    public void abrirTelaProntuarios() {
        telaProntuarios.setVisible(true);
    }
}
