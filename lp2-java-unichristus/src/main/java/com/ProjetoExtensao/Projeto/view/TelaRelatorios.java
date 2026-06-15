package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Prescricao;
import com.ProjetoExtensao.Projeto.models.Vacina;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.servicos.RelatorioService;
import com.ProjetoExtensao.Projeto.servicos.RelatorioIndividualDTO;
import com.ProjetoExtensao.Projeto.utils.CPFUtils;
import com.ProjetoExtensao.Projeto.utils.EventosOcorridos;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Tela central de relatórios e estatísticas da aplicação.
 *
 * <p>Organizada em três abas (JTabbedPane), cada uma com uma finalidade analítica distinta:</p>
 *
 * <h3>Aba 1 — Relatório Individual</h3>
 * <p>O usuário informa o CPF de um residente e o sistema exibe:</p>
 * <ul>
 *   <li>Ficha pessoal (nome, CPF, data de nascimento, status ativo/inativo)</li>
 *   <li>Tabela de prescrições médicas ativas do prontuário</li>
 *   <li>Tabela do histórico de vacinas aplicadas</li>
 * </ul>
 *
 * <h3>Aba 2 — Percentual de Vacinação</h3>
 * <p>O usuário informa o nome de uma vacina e o sistema calcula:</p>
 * <ul>
 *   <li>Percentual de cobertura vacinal dos residentes ativos (barra de progresso visual)</li>
 *   <li>Números absolutos: total de ativos, vacinados e não vacinados</li>
 * </ul>
 *
 * <h3>Aba 3 — Percentual de Incidentes</h3>
 * <p>Carregada automaticamente ao ser selecionada, exibe:</p>
 * <ul>
 *   <li>Percentual de residentes ativos acometidos por ao menos um evento sentinela</li>
 *   <li>Tabela de agrupamento de ocorrências por tipo de evento</li>
 * </ul>
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 * @see RelatorioService
 * @see RelatorioIndividualDTO
 */
@Component
@NoArgsConstructor
public class TelaRelatorios extends JFrame {

    // --- Dependências Spring ---

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PanelsFactory panelsFactory;

    /** Formatador de data para exibição no padrão brasileiro (dd/MM/yyyy). */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // =====================================================================
    // === Componentes da Aba 1 — Relatório Individual ====================
    // =====================================================================

    /** Campo mascarado para entrada do CPF na aba de relatório individual. */
    private JFormattedTextField txtCpfBusca;

    /** Labels de exibição dos dados cadastrais do residente encontrado. */
    private JLabel lblNomeValor;
    private JLabel lblCpfValor;
    private JLabel lblNascValor;
    private JLabel lblStatusValor;

    /** Modelo da tabela de prescrições médicas do relatório individual. */
    private DefaultTableModel modeloTabelaPrescricoes;

    /** Modelo da tabela de vacinas do relatório individual. */
    private DefaultTableModel modeloTabelaVacinas;

    // =====================================================================
    // === Componentes da Aba 2 — Percentual de Vacinação =================
    // =====================================================================

    /** Campo de texto para inserção do nome da vacina a ser calculada. */
    private JTextField txtNomeVacina;

    /** Barra de progresso que exibe visualmente o percentual de cobertura vacinal. */
    private JProgressBar progressVacina;

    /** Labels de estatísticas absolutas da vacinação. */
    private JLabel lblTotalAtivosVacina;
    private JLabel lblTotalVacinados;
    private JLabel lblTotalNaoVacinados;

    // =====================================================================
    // === Componentes da Aba 3 — Percentual de Incidentes ================
    // =====================================================================

    /** Barra de progresso que exibe visualmente o percentual de residentes com incidentes. */
    private JProgressBar progressIncidentes;

    /** Label com o total de residentes ativos e o percentual calculado. */
    private JLabel lblTotalAtivosIncidentes;

    /** Modelo da tabela de agrupamento de incidentes por tipo de evento. */
    private DefaultTableModel modeloTabelaIncidentes;

    // =====================================================================
    // === Inicialização da UI ============================================
    // =====================================================================

    /**
     * Inicializa e monta a estrutura completa da tela com suas três abas.
     *
     * <p>Chamado automaticamente pelo Spring após injeção das dependências.
     * Registra listener no {@code JTabbedPane} para carregar os dados de
     * incidentes automaticamente quando a aba 3 é selecionada.</p>
     */
    @PostConstruct
    public void initUI() {
        setTitle("Recanto do Sagrado Coração - Central de Relatórios e Estatísticas");
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header e Footer padronizados do sistema
        JPanel headerPanel = panelsFactory.getHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel footerPanel = panelsFactory.getFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);

        // Painel central com as abas de relatórios
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(Cores.COR_FUNDO_CLARO);
        tabbedPane.setForeground(Cores.COR_RODAPE);

        tabbedPane.addTab("Relatório Individual",      createAbaIndividual());
        tabbedPane.addTab("Percentual de Vacinação",   createAbaVacinacao());
        tabbedPane.addTab("Percentual de Incidentes",  createAbaIncidentes());

        add(tabbedPane, BorderLayout.CENTER);

        // Carrega dados de incidentes automaticamente ao selecionar a aba 3 (índice 2)
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex == 2) {
                carregarDadosIncidentes();
            }
        });
    }

    // =====================================================================
    // === ABA 1: RELATÓRIO INDIVIDUAL ====================================
    // =====================================================================

    /**
     * Monta o painel completo da aba "Relatório Individual".
     *
     * <p>Composta por:</p>
     * <ul>
     *   <li>Barra de busca por CPF com botão "Gerar Relatório"</li>
     *   <li>Ficha do residente (nome, CPF, nascimento, status)</li>
     *   <li>Split vertical com tabela de prescrições (topo) e tabela de vacinas (baixo)</li>
     * </ul>
     *
     * @return JPanel configurado e pronto para uso
     */
    private JPanel createAbaIndividual() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Cores.COR_FUNDO_CLARO);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // --- Painel de Busca ---
        JPanel panelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelBusca.setBackground(Cores.COR_BRANCO);
        panelBusca.setBorder(BorderFactory.createLineBorder(Cores.COR_BORDA));

        JLabel lblCpf = new JLabel("CPF do Paciente:");
        lblCpf.setFont(new Font("Arial", Font.BOLD, 14));
        lblCpf.setForeground(Cores.COR_RODAPE);

        // Campo CPF com máscara ###.###.###-##
        try {
            javax.swing.text.MaskFormatter mask = new javax.swing.text.MaskFormatter("###.###.###-##");
            mask.setPlaceholderCharacter('_');
            txtCpfBusca = new JFormattedTextField(mask);
            txtCpfBusca.setFont(new Font("Arial", Font.PLAIN, 14));
            txtCpfBusca.setColumns(12);
        } catch (Exception e) {
            txtCpfBusca = new JFormattedTextField();
        }

        JButton btnBuscar = new JButton("Gerar Relatório");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 13));
        btnBuscar.setBackground(Cores.COR_RODAPE);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(e -> buscarRelatorioIndividual());

        panelBusca.add(lblCpf);
        panelBusca.add(txtCpfBusca);
        panelBusca.add(btnBuscar);
        panel.add(panelBusca, BorderLayout.NORTH);

        // --- Painel de Dados ---
        JPanel panelDados = new JPanel(new GridBagLayout());
        panelDados.setBackground(Cores.COR_FUNDO_CLARO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;

        // Ficha do Residente (2 linhas x 4 colunas: label + valor)
        JPanel panelFicha = new JPanel(new GridLayout(2, 4, 15, 10));
        panelFicha.setBackground(Cores.COR_BRANCO);
        panelFicha.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Cores.COR_BORDA), "Ficha do Residente",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Cores.COR_RODAPE));

        panelFicha.add(new JLabel(" Nome Completo:"));
        lblNomeValor = new JLabel("-");
        lblNomeValor.setFont(new Font("Arial", Font.BOLD, 13));
        panelFicha.add(lblNomeValor);

        panelFicha.add(new JLabel(" CPF:"));
        lblCpfValor = new JLabel("-");
        lblCpfValor.setFont(new Font("Arial", Font.BOLD, 13));
        panelFicha.add(lblCpfValor);

        panelFicha.add(new JLabel(" Data Nascimento:"));
        lblNascValor = new JLabel("-");
        lblNascValor.setFont(new Font("Arial", Font.BOLD, 13));
        panelFicha.add(lblNascValor);

        panelFicha.add(new JLabel(" Status:"));
        lblStatusValor = new JLabel("-");
        lblStatusValor.setFont(new Font("Arial", Font.BOLD, 13));
        panelFicha.add(lblStatusValor);

        gbc.gridy = 0;
        gbc.weighty = 0.2;
        panelDados.add(panelFicha, gbc);

        // Split vertical: tabela de prescrições (topo) e tabela de vacinas (baixo)
        JSplitPane splitTables = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitTables.setResizeWeight(0.5);
        splitTables.setBorder(null);

        // Tabela de Prescrições Médicas
        String[] colPresc = {"Medicamento", "Dosagem", "Instruções", "Data"};
        modeloTabelaPrescricoes = new DefaultTableModel(colPresc, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tablePresc = new JTable(modeloTabelaPrescricoes);
        tablePresc.setRowHeight(25);
        tablePresc.getTableHeader().setBackground(Cores.COR_RODAPE);
        tablePresc.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPresc = new JScrollPane(tablePresc);
        scrollPresc.setBorder(BorderFactory.createTitledBorder("Prescrições Médicas Ativas"));

        // Tabela de Vacinas Aplicadas
        String[] colVac = {"Vacina", "Fabricante", "Lote", "Dosagem", "Data de Aplicação", "Responsável"};
        modeloTabelaVacinas = new DefaultTableModel(colVac, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tableVac = new JTable(modeloTabelaVacinas);
        tableVac.setRowHeight(25);
        tableVac.getTableHeader().setBackground(Cores.COR_RODAPE);
        tableVac.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollVac = new JScrollPane(tableVac);
        scrollVac.setBorder(BorderFactory.createTitledBorder("Vacinas Aplicadas"));

        splitTables.setTopComponent(scrollPresc);
        splitTables.setBottomComponent(scrollVac);

        gbc.gridy = 1;
        gbc.weighty = 0.8;
        panelDados.add(splitTables, gbc);

        panel.add(panelDados, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Executa a busca e popula o relatório individual com os dados do paciente.
     *
     * <p>Valida o CPF, chama {@link RelatorioService#gerarRelatorioIndividual(String)} e
     * preenche os labels de ficha e os modelos das tabelas de prescrições e vacinas.</p>
     */
    private void buscarRelatorioIndividual() {
        String cpf = txtCpfBusca.getText();
        String cpfLimpo = CPFUtils.limparCPF(cpf);
        if (!CPFUtils.validarTamanhoCPF(cpfLimpo)) {
            JOptionPane.showMessageDialog(this, "CPF inválido. Digite um CPF com 11 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            RelatorioIndividualDTO dto = relatorioService.gerarRelatorioIndividual(cpfLimpo);
            Paciente p = dto.getPaciente();

            // Preenche ficha do residente
            lblNomeValor.setText(p.getNomeCompleto());
            lblCpfValor.setText(CPFUtils.formatarCPF(p.getCpf()));
            lblNascValor.setText(p.getDataNascimento().format(DATE_FORMATTER));
            lblStatusValor.setText(p.getAtivo() ? "ATIVO (Residente)" : "INATIVO");
            lblStatusValor.setForeground(p.getAtivo() ? Cores.COR_VERDE_ENFERMARIA : Cores.COR_VERMELHO_IDOSAS);

            // Popula tabela de prescrições
            modeloTabelaPrescricoes.setRowCount(0);
            for (Prescricao pr : dto.getPrescricoes()) {
                modeloTabelaPrescricoes.addRow(new Object[]{
                        pr.getMedicamento(),
                        pr.getDosagem(),
                        pr.getInstrucoes(),
                        pr.getDataPrescricao().format(DATE_FORMATTER)
                });
            }

            // Popula tabela de vacinas
            modeloTabelaVacinas.setRowCount(0);
            for (Vacina v : dto.getVacinas()) {
                modeloTabelaVacinas.addRow(new Object[]{
                        v.getNomeVacina(),
                        v.getFabricante(),
                        v.getLote(),
                        v.getDosagem(),
                        v.getDataAplicacao().format(DATE_FORMATTER),
                        v.getResponsavelAplicacao()
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================================
    // === ABA 2: PERCENTUAL DE VACINAÇÃO ================================
    // =====================================================================

    /**
     * Monta o painel da aba "Percentual de Vacinação".
     *
     * <p>Inclui:</p>
     * <ul>
     *   <li>Campo para nome da vacina + botão "Calcular Percentual"</li>
     *   <li>{@link JProgressBar} colorido que representa a cobertura vacinal</li>
     *   <li>Labels com os números absolutos (total, vacinados, não vacinados)</li>
     * </ul>
     *
     * @return JPanel configurado e pronto para uso
     */
    private JPanel createAbaVacinacao() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Cores.COR_FUNDO_CLARO);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- Topo: campo de pesquisa ---
        JPanel panelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelTopo.setBackground(Cores.COR_BRANCO);
        panelTopo.setBorder(BorderFactory.createLineBorder(Cores.COR_BORDA));

        JLabel lblVacina = new JLabel("Nome da Vacina (ex: Gripe):");
        lblVacina.setFont(new Font("Arial", Font.BOLD, 14));
        lblVacina.setForeground(Cores.COR_RODAPE);

        txtNomeVacina = new JTextField(20);
        txtNomeVacina.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnCalcular = new JButton("Calcular Percentual");
        btnCalcular.setFont(new Font("Arial", Font.BOLD, 13));
        btnCalcular.setBackground(Cores.COR_RODAPE);
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setFocusPainted(false);
        btnCalcular.addActionListener(e -> calcularPercentualVacina());

        panelTopo.add(lblVacina);
        panelTopo.add(txtNomeVacina);
        panelTopo.add(btnCalcular);
        panel.add(panelTopo, BorderLayout.NORTH);

        // --- Centro: estatísticas visuais ---
        JPanel panelEstat = new JPanel(new GridBagLayout());
        panelEstat.setBackground(Cores.COR_BRANCO);
        panelEstat.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Cores.COR_BORDA),
                new EmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Título da barra de progresso
        JLabel lblProgresso = new JLabel("Percentual de Cobertura Vacinal (Residentes Ativos):", SwingConstants.CENTER);
        lblProgresso.setFont(new Font("Arial", Font.BOLD, 18));
        lblProgresso.setForeground(Cores.COR_RODAPE);
        gbc.gridy = 0;
        panelEstat.add(lblProgresso, gbc);

        // Barra de progresso verde (0-100%)
        progressVacina = new JProgressBar(0, 100);
        progressVacina.setStringPainted(true);
        progressVacina.setFont(new Font("Arial", Font.BOLD, 22));
        progressVacina.setForeground(Cores.COR_VERDE_ENFERMARIA);
        progressVacina.setBackground(new Color(230, 230, 230));
        progressVacina.setPreferredSize(new Dimension(500, 45));
        gbc.gridy = 1;
        panelEstat.add(progressVacina, gbc);

        // Labels de detalhamento numérico
        JPanel panelDet = new JPanel(new GridLayout(3, 1, 10, 10));
        panelDet.setOpaque(false);

        lblTotalAtivosVacina = new JLabel("Total de Residentes Ativos: -");
        lblTotalAtivosVacina.setFont(new Font("Arial", Font.PLAIN, 15));
        panelDet.add(lblTotalAtivosVacina);

        lblTotalVacinados = new JLabel("Vacinados (Com pelo menos uma aplicação): -");
        lblTotalVacinados.setFont(new Font("Arial", Font.PLAIN, 15));
        panelDet.add(lblTotalVacinados);

        lblTotalNaoVacinados = new JLabel("Não Vacinados: -");
        lblTotalNaoVacinados.setFont(new Font("Arial", Font.PLAIN, 15));
        panelDet.add(lblTotalNaoVacinados);

        gbc.gridy = 2;
        panelEstat.add(panelDet, gbc);

        panel.add(panelEstat, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Calcula e exibe o percentual de vacinação para a vacina informada pelo usuário.
     *
     * <p>Atualiza a barra de progresso e os labels com os valores calculados pelo
     * {@link RelatorioService#calcularPercentualVacinacao(String)}.</p>
     */
    private void calcularPercentualVacina() {
        String nomeVacina = txtNomeVacina.getText().trim();
        if (nomeVacina.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite o nome da vacina.", "Campo Vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            long totalAtivos = pacienteService.findPacientesByAtivo(true).size();
            double pct = relatorioService.calcularPercentualVacinacao(nomeVacina);
            long vacinados = Math.round((pct / 100.0) * totalAtivos);
            long naoVacinados = totalAtivos - vacinados;

            progressVacina.setValue((int) pct);
            lblTotalAtivosVacina.setText("Total de Residentes Ativos: " + totalAtivos);
            lblTotalVacinados.setText(String.format("Vacinados com '%s': %d (%.1f%%)", nomeVacina, vacinados, pct));
            lblTotalNaoVacinados.setText(String.format("Não Vacinados: %d (%.1f%%)", naoVacinados, (100.0 - pct)));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================================
    // === ABA 3: PERCENTUAL DE INCIDENTES ================================
    // =====================================================================

    /**
     * Monta o painel da aba "Percentual de Incidentes".
     *
     * <p>Inclui:</p>
     * <ul>
     *   <li>{@link JProgressBar} vermelho com o percentual de residentes acometidos</li>
     *   <li>Tabela de agrupamento de eventos sentinela por tipo e quantidade</li>
     * </ul>
     *
     * @return JPanel configurado e pronto para uso
     */
    private JPanel createAbaIncidentes() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Cores.COR_FUNDO_CLARO);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        // --- Painel de estatísticas visuais ---
        JPanel panelEstat = new JPanel(new GridBagLayout());
        panelEstat.setBackground(Cores.COR_BRANCO);
        panelEstat.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Cores.COR_BORDA),
                new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblProgresso = new JLabel("Percentual de Residentes Ativos com Incidentes:", SwingConstants.CENTER);
        lblProgresso.setFont(new Font("Arial", Font.BOLD, 18));
        lblProgresso.setForeground(Cores.COR_RODAPE);
        gbc.gridy = 0;
        panelEstat.add(lblProgresso, gbc);

        // Barra de progresso vermelha (cor de alerta para incidentes)
        progressIncidentes = new JProgressBar(0, 100);
        progressIncidentes.setStringPainted(true);
        progressIncidentes.setFont(new Font("Arial", Font.BOLD, 22));
        progressIncidentes.setForeground(Cores.COR_VERMELHO_IDOSAS);
        progressIncidentes.setBackground(new Color(230, 230, 230));
        progressIncidentes.setPreferredSize(new Dimension(500, 45));
        gbc.gridy = 1;
        panelEstat.add(progressIncidentes, gbc);

        lblTotalAtivosIncidentes = new JLabel("Total de Residentes Ativos: -", SwingConstants.CENTER);
        lblTotalAtivosIncidentes.setFont(new Font("Arial", Font.PLAIN, 15));
        gbc.gridy = 2;
        panelEstat.add(lblTotalAtivosIncidentes, gbc);

        panel.add(panelEstat, BorderLayout.NORTH);

        // --- Tabela de detalhamento por tipo de evento sentinela ---
        String[] colunas = {"Tipo de Evento Sentinela", "Quantidade Ocorrências"};
        modeloTabelaIncidentes = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable tableInc = new JTable(modeloTabelaIncidentes);
        tableInc.setRowHeight(25);
        tableInc.getTableHeader().setBackground(Cores.COR_RODAPE);
        tableInc.getTableHeader().setForeground(Color.WHITE);
        tableInc.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollTable = new JScrollPane(tableInc);
        scrollTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Cores.COR_BORDA), "Detalhamento por Ocorrências (Histórico Geral)"));

        panel.add(scrollTable, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Carrega e exibe os dados de incidentes sentinela em tempo real.
     *
     * <p>Chamado automaticamente pelo {@code ChangeListener} do {@code JTabbedPane}
     * sempre que a aba de incidentes é selecionada pelo usuário.
     * Calcula o percentual via {@link RelatorioService#calcularPercentualIncidentes()}
     * e agrupa os eventos por tipo via {@link RelatorioService#contarEventosPorTipo()}.</p>
     *
     * <p>O nome de cada tipo de evento é humanizado:
     * {@code QUEDA_DA_CAMA} → {@code "Queda da cama"}.</p>
     */
    private void carregarDadosIncidentes() {
        try {
            long totalAtivos = pacienteService.findPacientesByAtivo(true).size();
            double pct = relatorioService.calcularPercentualIncidentes();

            progressIncidentes.setValue((int) pct);
            lblTotalAtivosIncidentes.setText(String.format(
                    "Total de Residentes Ativos: %d | Percentual Acometido por Eventos: %.1f%%", totalAtivos, pct));

            // Popula a tabela de agrupamento por tipo de evento
            modeloTabelaIncidentes.setRowCount(0);
            Map<EventosOcorridos, Long> resumo = relatorioService.contarEventosPorTipo();

            for (Map.Entry<EventosOcorridos, Long> entry : resumo.entrySet()) {
                // Humaniza o nome do enum: QUEDA_DA_CAMA → Queda da cama
                String nomeFormatado = entry.getKey().name().replace("_", " ").toLowerCase();
                nomeFormatado = nomeFormatado.substring(0, 1).toUpperCase() + nomeFormatado.substring(1);
                modeloTabelaIncidentes.addRow(new Object[]{nomeFormatado, entry.getValue()});
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados de incidentes: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}