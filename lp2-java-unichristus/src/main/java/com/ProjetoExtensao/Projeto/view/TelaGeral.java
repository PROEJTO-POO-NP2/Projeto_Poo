package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.IconManager;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Tela Geral (Dashboard Administrativo).
 *
 * Tela principal do sistema após o login, fornecendo acesso a todos os módulos
 * como Pacientes, Consultas, Prontuários, Eventos Sentinelas, etc.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@org.springframework.stereotype.Component
@NoArgsConstructor
public class TelaGeral extends JFrame {
    @Autowired
    private PanelsFactory panelsFactory;
    @Autowired
    private IconManager iconManager;
    @Autowired
    private NavigationService navigationService;
    @Autowired
    private PacienteService pacienteService;


    @PostConstruct
    private void initUI() {
        setTitle("Recanto do Sagrado Coração - Painel Administrativo");
        setSize(900, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Cores.COR_FUNDO_ESCURO);

        add(panelsFactory.getHeaderPanel(), BorderLayout.NORTH);
        add(panelsFactory.getFooterPanel(), BorderLayout.SOUTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(Cores.COR_FUNDO_CLARO);
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Painel Administrativo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Cores.COR_LETRA_PAINEL);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        statsPanel.setOpaque(false);
        long totalPacientes = pacienteService.findPacientesByAtivo(true).size();
        statsPanel.add(createStatItem("Pacientes Ativos", String.valueOf(totalPacientes), Cores.COR_VERMELHO_IDOSAS));
        statsPanel.add(createStatItem("Enfermaria", "20", Cores.COR_VERDE_ENFERMARIA));
        statsPanel.add(createStatItem("Visitas", "2", Cores.COR_VERDE_ENFERMARIA));

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        gridPanel.setOpaque(false);

        // Botão Pacientes
        JButton btnPacientes = createDashboardButton("Pacientes", "pacientes.png");
        btnPacientes.addActionListener(e -> {
            navigationService.abrirTelaPacientes();
            dispose();
        });

        // Botão Consultas
        JButton btnConsultas = createDashboardButton("Consultas", "consultas.png");
        btnConsultas.addActionListener(e -> {
            navigationService.abrirTelaConsultas();
            dispose();
        });

        // Botão Eventos Sentinelas
        JButton btnEventos = createDashboardButton("Eventos Sentinelas", "eventos.png");
        btnEventos.addActionListener(e -> {
            navigationService.abrirTelaEventosSentinelas();
            dispose();
        });

        // Botão Família (Em desenvolvimento)
        JButton btnFamilia = createDashboardButton("Família", "familia.png");
        btnFamilia.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Módulo de Família em desenvolvimento.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        });

        // Botão Documentos (Em desenvolvimento)
        JButton btnDocumentos = createDashboardButton("Documentos", "documentos.png");
        btnDocumentos.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Módulo de Documentos em desenvolvimento.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        });

        gridPanel.add(btnPacientes);
        gridPanel.add(btnFamilia);
        gridPanel.add(btnDocumentos);
        gridPanel.add(btnEventos);
        
        JButton btnProntuarios = createDashboardButton("Prontuários", "prontuarios.png");
        btnProntuarios.addActionListener(e -> {
            navigationService.abrirTelaProntuarios();
            dispose();
        });
        gridPanel.add(btnProntuarios);

        gridPanel.add(btnConsultas);

        // Botão Vacinas
        JButton btnVacinas = createDashboardButton("Vacinas", "vacinas.png");
        btnVacinas.addActionListener(e -> {
            navigationService.abrirTelaVacinas();
            dispose();
        });
        gridPanel.add(btnVacinas);

        // Botão Relatórios
        JButton btnRelatorios = createDashboardButton("Relatórios", "relatorios.png");
        btnRelatorios.addActionListener(e -> {
            navigationService.abrirTelaRelatorios();
            dispose();
        });
        gridPanel.add(btnRelatorios);

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createStatItem(String title, String value, Color valueColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setForeground(Cores.COR_LETRA_PAINEL);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(valueColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(valueLabel);

        return panel;
    }

    private JButton createDashboardButton(String text, String iconName) {
        ImageIcon icon = iconManager.createScaledIcon("/images/" + iconName, 48, 48);

        JButton button = new JButton(text, icon);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Cores.COR_LETRA_PAINEL);
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito de hover moderno
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Cores.COR_FUNDO_CLARO);
                button.setBorder(BorderFactory.createLineBorder(Cores.COR_RODAPE));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));
            }
        });

        return button;
    }
}
