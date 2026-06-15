package com.ProjetoExtensao.Projeto.view; // 1. Garante o pacote correto

import com.ProjetoExtensao.Projeto.servicos.RelatorioService; // 2. Importa o Service correto
import com.ProjetoExtensao.Projeto.models.Paciente;           // 3. Importa o modelo Paciente

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List; // 4. Importa a estrutura de Listas do Java

public class TelaRelatorios extends JFrame {

    private RelatorioService relatorioService;
    private JButton btnGerarRelatorio;
    private JTextField txtDataInicio; 
    private JTextField txtDataFim;    

    // O construtor recebe o Service para poder buscar os dados do banco
    public TelaRelatorios(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
        this.setTitle("Módulo de Relatórios por Período");
        initComponents();
    }

    private void initComponents() {
        // ... Configurações normais de layout da sua tela ...

        btnGerarRelatorio = new JButton("Gerar Relatório");
        txtDataInicio = new JTextField(10);
        txtDataFim = new JTextField(10);

        // AÇÃO DO BOTÃO: Conectando o clique ao RelatorioService
        btnGerarRelatorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Define o formato esperado digitado pelo usuário (ex: 25/12/2026)
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    
                    LocalDate inicio = LocalDate.parse(txtDataInicio.getText(), formatter);
                    LocalDate fim = LocalDate.parse(txtDataFim.getText(), formatter);
                    
                    // 5. Especificamos explicitamente que o retorno é uma List<Paciente>
                    List<Paciente> dadosFiltrados = relatorioService.gerarRelatorioPorPeriodo(inicio, fim);
                    
                    // Exemplo de sucesso: exibe a quantidade de registros encontrados
                    JOptionPane.showMessageDialog(null, "Relatório gerado com sucesso! Registros encontrados: " + dadosFiltrados.size());
                    
                    // TODO: Chame aqui o método para renderizar os 'dadosFiltrados' na sua JTable
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao processar o período: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}