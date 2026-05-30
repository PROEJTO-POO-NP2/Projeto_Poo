package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.FormatadorDataHora;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.models.*;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.servicos.ProntuarioService;
import com.ProjetoExtensao.Projeto.utils.CPFUtils;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Tela de gerenciamento de Prontuários Médicos.
 *
 * Permite buscar prontuários por CPF do paciente, visualizar
 * consultas, exames e prescrições vinculadas, além de criar
 * novos prontuários e adicionar registros.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@org.springframework.stereotype.Component
@NoArgsConstructor
public class TelaProntuarios extends JFrame {

    @Autowired
    private PanelsFactory panelsFactory;
    @Autowired
    private NavigationService navigationService;
    @Autowired
    private ProntuarioService prontuarioService;
    @Autowired
    private PacienteService pacienteService;

    // === Campos de pesquisa ===
    private JTextField cpfPesquisaField;

    // === Dados do prontuário ===
    private JTextField prontuarioIdField;
    private JTextField pacienteNomeField;
    private JTextField dataCriacaoField;
    private JTextArea observacoesArea;

    // === Tabelas ===
    private DefaultTableModel modeloConsultas;
    private DefaultTableModel modeloExames;
    private DefaultTableModel modeloPrescricoes;

    // === Prontuário atual carregado ===
    private Prontuario prontuarioAtual;

    /**
     * Inicializa a interface gráfica da tela de prontuários.
     * Chamado automaticamente pelo Spring após a injeção de dependências.
     */
    @PostConstruct
    private void initUI() {
        setTitle("Recanto do Sagrado Coração - Prontuários Médicos");
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Cores.COR_FUNDO_ESCURO);

        /* Cabeçalho e rodapé institucionais compartilhados */
        add(panelsFactory.getHeaderPanel(), BorderLayout.NORTH);
        add(panelsFactory.getFooterPanel(), BorderLayout.SOUTH);
        add(criarPainelPrincipal(), BorderLayout.CENTER);
    }

    /**
     * Cria o painel principal com todas as seções da tela.
     */
    private JPanel criarPainelPrincipal() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Cores.COR_FUNDO_CLARO);
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        Color azulEscuro = Cores.COR_RODAPE;
        Color cinzaTitulo = Cores.COR_LETRA_PAINEL;

        // === CABEÇALHO DE SEÇÃO ===
        JPanel sectionHeader = new JPanel(new BorderLayout(10, 0));
        sectionHeader.setBackground(Cores.COR_FUNDO_CLARO);
        sectionHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, cinzaTitulo));
        sectionHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        sectionHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Prontuários Médicos");
        sectionTitle.setFont(new Font("Arial", Font.PLAIN, 36));
        sectionTitle.setForeground(cinzaTitulo);
        sectionTitle.setBorder(new EmptyBorder(0, 20, 0, 0));
        sectionHeader.add(sectionTitle, BorderLayout.WEST);

        /* Botão de voltar ao dashboard */
        JPanel sectionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sectionButtonsPanel.setOpaque(false);

        JButton voltarBtn = new JButton("\u2190 Voltar ao Dashboard");
        voltarBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        voltarBtn.setForeground(cinzaTitulo);
        voltarBtn.setContentAreaFilled(false);
        voltarBtn.setBorderPainted(false);
        voltarBtn.setFocusPainted(false);
        voltarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        voltarBtn.addActionListener(e -> {
            navigationService.abrirTelaGeral();
            dispose();
        });
        sectionButtonsPanel.add(voltarBtn);
        sectionHeader.add(sectionButtonsPanel, BorderLayout.EAST);

        mainPanel.add(sectionHeader);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // === SEÇÃO DE PESQUISA ===
        JPanel pesquisaPanel = new JPanel(new GridBagLayout());
        pesquisaPanel.setBackground(Cores.COR_FUNDO_CLARO);
        pesquisaPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        pesquisaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pesquisaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        /* Título da seção de pesquisa */
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3; gbc.weightx = 1.0;
        JLabel pesquisaTitulo = new JLabel("Buscar Prontuário por CPF do Paciente");
        pesquisaTitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        pesquisaTitulo.setForeground(new Color(0x556270));
        pesquisaPanel.add(pesquisaTitulo, gbc);

        /* Label do campo CPF */
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.0;
        JLabel cpfLabel = new JLabel("CPF do Paciente:");
        cpfLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cpfLabel.setForeground(azulEscuro);
        pesquisaPanel.add(cpfLabel, gbc);

        /* Campo de entrada CPF */
        gbc.gridx = 1; gbc.weightx = 1.0;
        cpfPesquisaField = new JTextField();
        cpfPesquisaField.setFont(new Font("Arial", Font.PLAIN, 16));
        cpfPesquisaField.setForeground(azulEscuro);
        CPFUtils.aplicarFormatacaoAutomatica(cpfPesquisaField);
        pesquisaPanel.add(cpfPesquisaField, gbc);

        /* Botão Pesquisar */
        gbc.gridx = 2; gbc.weightx = 0.0;
        JButton pesquisarBtn = criarBotao("Pesquisar");
        pesquisarBtn.addActionListener(e -> pesquisarProntuario());
        pesquisaPanel.add(pesquisarBtn, gbc);

        mainPanel.add(pesquisaPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // === PAINEL DE DETALHES DO PRONTUÁRIO ===
        JPanel detalhesPanel = new JPanel(new GridBagLayout());
        detalhesPanel.setBackground(Color.WHITE);
        detalhesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        detalhesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 5, 3, 5);

        /* Título */
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4; gbc.weightx = 1.0;
        JLabel detalhesTitulo = new JLabel("Dados do Prontuário");
        detalhesTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        detalhesTitulo.setForeground(azulEscuro);
        detalhesPanel.add(detalhesTitulo, gbc);

        /* Linha 1: ID e Data */
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.0;
        detalhesPanel.add(criarLabel("Prontuário Nº:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.5;
        prontuarioIdField = criarCampoReadonly();
        detalhesPanel.add(prontuarioIdField, gbc);

        gbc.gridx = 2; gbc.weightx = 0.0;
        detalhesPanel.add(criarLabel("Data de Criação:"), gbc);

        gbc.gridx = 3; gbc.weightx = 0.5;
        dataCriacaoField = criarCampoReadonly();
        detalhesPanel.add(dataCriacaoField, gbc);

        /* Linha 2: Paciente */
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        detalhesPanel.add(criarLabel("Paciente:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        pacienteNomeField = criarCampoReadonly();
        detalhesPanel.add(pacienteNomeField, gbc);

        /* Linha 3: Observações */
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0.0;
        detalhesPanel.add(criarLabel("Observações:"), gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 3; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.3;
        observacoesArea = new JTextArea(3, 20);
        observacoesArea.setFont(new Font("Arial", Font.PLAIN, 14));
        observacoesArea.setLineWrap(true);
        observacoesArea.setWrapStyleWord(true);
        observacoesArea.setEditable(false);
        observacoesArea.setBorder(BorderFactory.createLineBorder(Cores.COR_BORDA));
        detalhesPanel.add(new JScrollPane(observacoesArea), gbc);

        /* Botões de ação */
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        botoesPanel.setOpaque(false);

        JButton criarProntuarioBtn = criarBotao("Criar Prontuário");
        criarProntuarioBtn.addActionListener(e -> criarProntuario());
        botoesPanel.add(criarProntuarioBtn);

        JButton addExameBtn = criarBotao("Adicionar Exame");
        addExameBtn.addActionListener(e -> adicionarExame());
        botoesPanel.add(addExameBtn);

        JButton addPrescricaoBtn = criarBotao("Adicionar Prescrição");
        addPrescricaoBtn.addActionListener(e -> adicionarPrescricao());
        botoesPanel.add(addPrescricaoBtn);

        JButton resumoBtn = criarBotao("Gerar Resumo");
        resumoBtn.addActionListener(e -> gerarResumo());
        botoesPanel.add(resumoBtn);

        detalhesPanel.add(botoesPanel, gbc);

        mainPanel.add(detalhesPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // === ABAS DE CONSULTAS / EXAMES / PRESCRIÇÕES ===
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        /* Aba Consultas */
        String[] colunasConsultas = {"ID", "Data", "Hora", "Tipo", "Médico", "Diagnóstico"};
        modeloConsultas = new DefaultTableModel(colunasConsultas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabelaConsultas = new JTable(modeloConsultas);
        tabelaConsultas.setRowHeight(25);
        tabbedPane.addTab("Consultas", new JScrollPane(tabelaConsultas));

        /* Aba Exames */
        String[] colunasExames = {"ID", "Nome do Exame", "Data", "Resultado"};
        modeloExames = new DefaultTableModel(colunasExames, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabelaExames = new JTable(modeloExames);
        tabelaExames.setRowHeight(25);
        tabbedPane.addTab("Exames", new JScrollPane(tabelaExames));

        /* Aba Prescrições */
        String[] colunasPrescricoes = {"ID", "Medicamento", "Dosagem", "Instruções", "Data"};
        modeloPrescricoes = new DefaultTableModel(colunasPrescricoes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabelaPrescricoes = new JTable(modeloPrescricoes);
        tabelaPrescricoes.setRowHeight(25);
        tabbedPane.addTab("Prescrições", new JScrollPane(tabelaPrescricoes));

        mainPanel.add(tabbedPane);

        return mainPanel;
    }

    // ========== Ações dos botões ==========

    /**
     * Pesquisa o prontuário pelo CPF digitado.
     * Se não existir, informa ao usuário e oferece a opção de criar.
     */
    private void pesquisarProntuario() {
        String cpfTexto = cpfPesquisaField.getText();
        String cpfLimpo = CPFUtils.limparCPF(cpfTexto);

        if (cpfLimpo.isEmpty() || !CPFUtils.validarTamanhoCPF(cpfLimpo)) {
            JOptionPane.showMessageDialog(this,
                    "CPF inválido. Digite um CPF com 11 dígitos.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<Prontuario> prontuarioOpt = prontuarioService.buscarPorCpf(cpfLimpo);

        if (prontuarioOpt.isPresent()) {
            prontuarioAtual = prontuarioOpt.get();
            carregarDadosProntuario();
        } else {
            limparCampos();
            /* Verifica se o paciente existe, mesmo sem prontuário */
            try {
                Paciente paciente = pacienteService.findPacienteByCpf(cpfLimpo);
                int resposta = JOptionPane.showConfirmDialog(this,
                        "Paciente encontrado: " + paciente.getNomeCompleto() +
                                "\n\nEste paciente ainda não possui um prontuário.\nDeseja criar um agora?",
                        "Prontuário não encontrado",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (resposta == JOptionPane.YES_OPTION) {
                    criarProntuarioParaPaciente(paciente);
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this,
                        "Paciente não encontrado com o CPF: " + cpfLimpo +
                                "\n\nVerifique se o paciente está cadastrado no sistema.",
                        "Paciente não encontrado", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Carrega os dados do prontuário atual nos campos da tela e nas tabelas.
     */
    private void carregarDadosProntuario() {
        if (prontuarioAtual == null) return;

        prontuarioIdField.setText(prontuarioAtual.getId().toString());
        pacienteNomeField.setText(prontuarioAtual.getPaciente().getNomeCompleto() +
                " — CPF: " + prontuarioAtual.getPaciente().getCpf());
        dataCriacaoField.setText(prontuarioAtual.getDataCriacao()
                .format(FormatadorDataHora.DATE_TIME_FORMATTER));
        observacoesArea.setText(prontuarioAtual.getObservacoesGerais() != null
                ? prontuarioAtual.getObservacoesGerais() : "");

        /* Preencher tabela de consultas */
        modeloConsultas.setRowCount(0);
        for (Consulta c : prontuarioAtual.getConsultas()) {
            modeloConsultas.addRow(new Object[]{
                    c.getId(),
                    c.getData().format(FormatadorDataHora.DATE_TIME_FORMATTER),
                    c.getHora().toString(),
                    c.getTipoConsulta().toString(),
                    c.getResponsavelSaude().getNomeCompleto(),
                    c.getDiagnostico() != null ? c.getDiagnostico() : "—"
            });
        }

        /* Preencher tabela de exames */
        modeloExames.setRowCount(0);
        for (Exame ex : prontuarioAtual.getExames()) {
            modeloExames.addRow(new Object[]{
                    ex.getId(),
                    ex.getNomeExame(),
                    ex.getDataExame().format(FormatadorDataHora.DATE_TIME_FORMATTER),
                    ex.getResultado() != null ? ex.getResultado() : "Aguardando resultado"
            });
        }

        /* Preencher tabela de prescrições */
        modeloPrescricoes.setRowCount(0);
        for (Prescricao p : prontuarioAtual.getPrescricoes()) {
            modeloPrescricoes.addRow(new Object[]{
                    p.getId(),
                    p.getMedicamento(),
                    p.getDosagem(),
                    p.getInstrucoes() != null ? p.getInstrucoes() : "—",
                    p.getDataPrescricao().format(FormatadorDataHora.DATE_TIME_FORMATTER)
            });
        }
    }

    /**
     * Cria um novo prontuário para o paciente encontrado na pesquisa.
     */
    private void criarProntuario() {
        String cpfLimpo = CPFUtils.limparCPF(cpfPesquisaField.getText());
        if (cpfLimpo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Pesquise um paciente pelo CPF primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (prontuarioAtual != null) {
            JOptionPane.showMessageDialog(this,
                    "Este paciente já possui um prontuário (Nº " + prontuarioAtual.getId() + ").",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Paciente paciente = pacienteService.findPacienteByCpf(cpfLimpo);
            criarProntuarioParaPaciente(paciente);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Paciente não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cria e salva o prontuário para um paciente específico.
     */
    private void criarProntuarioParaPaciente(Paciente paciente) {
        String observacoes = JOptionPane.showInputDialog(this,
                "Observações iniciais para o prontuário de " + paciente.getNomeCompleto() + ":",
                "Criar Prontuário", JOptionPane.PLAIN_MESSAGE);

        Prontuario novo = new Prontuario(paciente, LocalDate.now(),
                observacoes != null ? observacoes : "");
        prontuarioAtual = prontuarioService.salvarProntuario(novo);
        carregarDadosProntuario();

        JOptionPane.showMessageDialog(this,
                "✓ Prontuário criado com sucesso!\n\nProntuário Nº: " + prontuarioAtual.getId(),
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Abre um diálogo para adicionar um exame ao prontuário atual.
     */
    private void adicionarExame() {
        if (prontuarioAtual == null) {
            JOptionPane.showMessageDialog(this,
                    "Carregue um prontuário primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField nomeExameField = new JTextField();
        JTextField resultadoField = new JTextField();

        Object[] campos = {
                "Nome do Exame:", nomeExameField,
                "Resultado (deixe vazio se ainda não disponível):", resultadoField
        };

        int resultado = JOptionPane.showConfirmDialog(this, campos,
                "Adicionar Exame", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION && !nomeExameField.getText().isBlank()) {
            Exame exame = new Exame(prontuarioAtual, nomeExameField.getText().trim(),
                    LocalDate.now(),
                    resultadoField.getText().isBlank() ? null : resultadoField.getText().trim());
            prontuarioService.adicionarExame(prontuarioAtual.getId(), exame);

            /* Recarregar os dados do prontuário para atualizar as tabelas */
            prontuarioAtual = prontuarioService.buscarPorId(prontuarioAtual.getId()).orElse(prontuarioAtual);
            carregarDadosProntuario();

            JOptionPane.showMessageDialog(this, "✓ Exame adicionado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Abre um diálogo para adicionar uma prescrição ao prontuário atual.
     */
    private void adicionarPrescricao() {
        if (prontuarioAtual == null) {
            JOptionPane.showMessageDialog(this,
                    "Carregue um prontuário primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField medicamentoField = new JTextField();
        JTextField dosagemField = new JTextField();
        JTextField instrucoesField = new JTextField();

        Object[] campos = {
                "Medicamento:", medicamentoField,
                "Dosagem:", dosagemField,
                "Instruções:", instrucoesField
        };

        int resultado = JOptionPane.showConfirmDialog(this, campos,
                "Adicionar Prescrição", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION && !medicamentoField.getText().isBlank()
                && !dosagemField.getText().isBlank()) {
            Prescricao prescricao = new Prescricao(prontuarioAtual,
                    medicamentoField.getText().trim(),
                    dosagemField.getText().trim(),
                    instrucoesField.getText().isBlank() ? null : instrucoesField.getText().trim(),
                    LocalDate.now());
            prontuarioService.adicionarPrescricao(prontuarioAtual.getId(), prescricao);

            /* Recarregar os dados do prontuário para atualizar as tabelas */
            prontuarioAtual = prontuarioService.buscarPorId(prontuarioAtual.getId()).orElse(prontuarioAtual);
            carregarDadosProntuario();

            JOptionPane.showMessageDialog(this, "✓ Prescrição adicionada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Gera e exibe o resumo textual completo do prontuário atual.
     */
    private void gerarResumo() {
        if (prontuarioAtual == null) {
            JOptionPane.showMessageDialog(this,
                    "Carregue um prontuário primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String resumo = prontuarioService.gerarResumoHistorico(prontuarioAtual.getId());

        JTextArea resumoArea = new JTextArea(resumo);
        resumoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resumoArea.setEditable(false);
        resumoArea.setLineWrap(true);
        resumoArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(resumoArea);
        scrollPane.setPreferredSize(new Dimension(600, 450));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Resumo do Prontuário — " + prontuarioAtual.getPaciente().getNomeCompleto(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Limpa todos os campos e tabelas da tela.
     */
    private void limparCampos() {
        prontuarioAtual = null;
        prontuarioIdField.setText("");
        pacienteNomeField.setText("");
        dataCriacaoField.setText("");
        observacoesArea.setText("");
        modeloConsultas.setRowCount(0);
        modeloExames.setRowCount(0);
        modeloPrescricoes.setRowCount(0);
    }

    // ========== Métodos utilitários de UI ==========

    /**
     * Cria um JTextField estilizado e somente leitura.
     */
    private JTextField criarCampoReadonly() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setEditable(false);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(Cores.COR_BORDA));
        return field;
    }

    /**
     * Cria um JLabel estilizado para rótulos de campos.
     */
    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Cores.COR_RODAPE);
        return label;
    }

    /**
     * Cria um JButton estilizado com as cores institucionais.
     */
    private JButton criarBotao(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(Cores.COR_RODAPE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
