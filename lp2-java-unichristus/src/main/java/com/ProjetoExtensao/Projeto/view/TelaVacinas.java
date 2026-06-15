package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.infra.PanelsFactory;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Vacina;
import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.servicos.VacinaService;
import com.ProjetoExtensao.Projeto.utils.CPFUtils;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tela principal do módulo de Controle de Vacinas.
 *
 * <p>Exibe o histórico vacinal de um residente pesquisado pelo CPF e permite
 * o acesso ao formulário de registro de novas vacinas ({@link TelaCadastroVacina}).</p>
 *
 * <h3>Fluxo de uso:</h3>
 * <ol>
 *   <li>O usuário digita o CPF do residente no campo de busca.</li>
 *   <li>Ao clicar em "Pesquisar", o sistema busca o paciente no banco e
 *       preenche a tabela com suas vacinas ordenadas da mais recente para a mais antiga.</li>
 *   <li>O botão "Registrar Vacina" abre o formulário {@link TelaCadastroVacina}.</li>
 *   <li>Ao salvar uma nova vacina, o método {@link #atualizarTabelaAposCadastro(Paciente)}
 *       é chamado para atualizar a tabela automaticamente.</li>
 * </ol>
 *
 * <p>Como todos os JFrames gerenciados pelo Spring neste sistema, esta tela usa
 * {@code @PostConstruct} para inicializar a UI após a injeção de dependências.</p>
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 * @see VacinaService
 * @see TelaCadastroVacina
 */
@Component
@NoArgsConstructor
public class TelaVacinas extends JFrame {

    // --- Dependências Spring injetadas automaticamente ---

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private VacinaService vacinaService;

    @Autowired
    private NavigationService navigationService;

    /** Formulário modal de cadastro de vacinas, aberto pelo botão "Registrar Vacina". */
    @Autowired
    private TelaCadastroVacina telaCadastroVacina;

    /** Fábrica de painéis reutilizáveis (Header e Footer padronizados da instituição). */
    @Autowired
    private PanelsFactory panelsFactory;

    // --- Componentes da UI declarados como atributos de instância ---

    /** Campo de texto para digitação do CPF do paciente a ser pesquisado. */
    private JTextField txtCpfBusca;

    /** Tabela que exibe o histórico de vacinas do paciente selecionado. */
    private JTable tabelaVacinas;

    /** Modelo de dados da tabela de vacinas, permite adicionar e remover linhas dinamicamente. */
    private DefaultTableModel modeloTabela;

    /** Botão de refresh do header, limpa a pesquisa ao ser clicado. */
    private JButton refreshButton;

    /** Paciente atualmente selecionado pela pesquisa de CPF. Nulo se nenhum foi pesquisado. */
    private Paciente pacienteAtual = null;

    /** Formatador de data para exibição no padrão brasileiro (dd/MM/yyyy). */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Inicializa e monta todos os componentes da tela.
     *
     * <p>Chamado automaticamente pelo Spring após a injeção de todas as dependências.
     * Configura o layout principal, header, footer, painel de pesquisa e tabela de vacinas.</p>
     */
    @PostConstruct
    public void initUI() {
        setTitle("Recanto do Sagrado Coração - Controle de Vacinas");
        setSize(1200, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Limpa a pesquisa sempre que a tela é exibida novamente
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                limparCamposPesquisa();
            }
        });

        Color azulEscuro = Cores.COR_RODAPE;
        Color cinzaTitulo = Cores.COR_LETRA_PAINEL;

        // Header padronizado do sistema (logo + botões de administração)
        JPanel headerPanel = panelsFactory.getHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        this.refreshButton = panelsFactory.getRefreshButton();

        // Footer padronizado do sistema
        JPanel footerPanel = panelsFactory.getFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);

        // --- Painel Central ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Cores.COR_FUNDO_CLARO);
        contentPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        add(contentPanel, BorderLayout.CENTER);

        // --- Seção: Título e botão "Registrar Vacina" ---
        JPanel sectionHeader = new JPanel(new BorderLayout(10, 0));
        sectionHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Cores.COR_LETRA_PAINEL));
        sectionHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        sectionHeader.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Histórico de Vacinas");
        sectionTitle.setFont(new Font("Arial", Font.PLAIN, 36));
        sectionTitle.setForeground(cinzaTitulo);
        sectionTitle.setBorder(new EmptyBorder(0, 20, 0, 0));
        sectionHeader.add(sectionTitle, BorderLayout.WEST);

        JPanel sectionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sectionButtonsPanel.setOpaque(false);

        JButton novaVacinaBtn = new JButton("Registrar Vacina");
        novaVacinaBtn.setFont(new Font("Arial", Font.BOLD, 16));
        novaVacinaBtn.setBackground(azulEscuro);
        novaVacinaBtn.setForeground(Color.WHITE);
        novaVacinaBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        novaVacinaBtn.setFocusPainted(false);
        novaVacinaBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sectionButtonsPanel.add(novaVacinaBtn);

        novaVacinaBtn.addActionListener(e -> abrirCadastroVacina());

        sectionHeader.add(sectionButtonsPanel, BorderLayout.EAST);
        contentPanel.add(sectionHeader);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Seção: Painel de Pesquisa por CPF ---
        JPanel pesquisaPanel = new JPanel();
        pesquisaPanel.setLayout(new GridBagLayout());
        pesquisaPanel.setBackground(Cores.COR_FUNDO_CLARO);
        pesquisaPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        pesquisaPanel.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        pesquisaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JPanel tituloPesquisaPanel = new JPanel(new BorderLayout());
        tituloPesquisaPanel.setBackground(Cores.COR_FUNDO_CLARO);
        tituloPesquisaPanel.setBorder(new EmptyBorder(0, 5, 0, 0));
        JLabel paramPesquisaLabel = new JLabel("Pesquisar por Paciente");
        paramPesquisaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        paramPesquisaLabel.setForeground(new Color(0x556270));
        tituloPesquisaPanel.add(paramPesquisaLabel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        pesquisaPanel.add(tituloPesquisaPanel, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0.5;

        JLabel cpfLabel = new JLabel("CPF do Paciente");
        cpfLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cpfLabel.setForeground(azulEscuro);
        pesquisaPanel.add(cpfLabel, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        txtCpfBusca = new JTextField();
        txtCpfBusca.setFont(new Font("Arial", Font.PLAIN, 16));
        txtCpfBusca.setForeground(azulEscuro);
        CPFUtils.aplicarFormatacaoAutomatica(txtCpfBusca);
        addPlaceholder(txtCpfBusca, "Digite o CPF do paciente");
        pesquisaPanel.add(txtCpfBusca, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 0.0;
        JButton pesquisarBtn = new JButton("Pesquisar");
        pesquisarBtn.setFont(new Font("Arial", Font.BOLD, 14));
        pesquisarBtn.setBackground(azulEscuro);
        pesquisarBtn.setForeground(Color.WHITE);
        pesquisarBtn.setFocusPainted(false);
        pesquisarBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pesquisarBtn.setBorder(new EmptyBorder(8, 15, 8, 15));
        pesquisaPanel.add(pesquisarBtn, gbc);

        contentPanel.add(pesquisaPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Seção: Tabela de Vacinas ---
        // Colunas que espelham os campos da entidade Vacina
        String[] colunas = {"ID", "Vacina", "Fabricante", "Lote", "Dosagem", "Data de Aplicação", "Responsável"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            /**
             * Impede a edição direta de células; dados são somente leitura na listagem.
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaVacinas = new JTable(modeloTabela);
        tabelaVacinas.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaVacinas.setRowHeight(30);
        tabelaVacinas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabelaVacinas.getTableHeader().setBackground(azulEscuro);
        tabelaVacinas.getTableHeader().setForeground(Color.WHITE);
        tabelaVacinas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tabelaVacinas);
        scrollPane.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentPanel.add(scrollPane);

        // Botão de refresh do header: limpa pesquisa e tabela
        if (refreshButton != null) {
            refreshButton.addActionListener(e -> {
                limparTabela();
                txtCpfBusca.setText("");
                addPlaceholder(txtCpfBusca, "Digite o CPF do paciente");
                pacienteAtual = null;
            });
        }

        pesquisarBtn.addActionListener(e -> buscarPaciente());
    }

    /**
     * Executa a busca de um paciente pelo CPF digitado no campo de pesquisa.
     *
     * <p>Valida o CPF antes de realizar a consulta. Em caso de sucesso, popula a tabela
     * com o histórico de vacinas do paciente encontrado. Em caso de erro (paciente não
     * encontrado ou CPF inválido), exibe um diálogo informativo ao usuário.</p>
     */
    private void buscarPaciente() {
        String cpf = txtCpfBusca.getText();
        if (cpf.isEmpty() || cpf.equals("Digite o CPF do paciente")) {
            JOptionPane.showMessageDialog(this, "Por favor, digite um CPF para pesquisar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String cpfLimpo = CPFUtils.limparCPF(cpf);
            if (!CPFUtils.validarTamanhoCPF(cpfLimpo)) {
                JOptionPane.showMessageDialog(this, "CPF inválido. Digite um CPF com 11 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                pacienteAtual = pacienteService.findPacienteByCpf(cpfLimpo);
                atualizarTabelaVacinas();
            } catch (RuntimeException ex) {
                pacienteAtual = null;
                limparTabela();
                JOptionPane.showMessageDialog(this, "Paciente não encontrado.", "Erro", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            limparTabela();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Recarrega a tabela com as vacinas do {@link #pacienteAtual}.
     *
     * <p>Consulta o serviço e popula o modelo da tabela com os registros ordenados por
     * data de aplicação decrescente. Se nenhuma vacina for encontrada, exibe aviso.</p>
     */
    public void atualizarTabelaVacinas() {
        limparTabela();
        if (pacienteAtual == null) return;

        List<Vacina> vacinas = vacinaService.findByPacienteOrderByDataAplicacaoDesc(pacienteAtual);
        if (vacinas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma vacina registrada para este paciente.", "Sem Registros", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Vacina v : vacinas) {
            Object[] linha = {
                v.getId(),
                v.getNomeVacina(),
                v.getFabricante(),
                v.getLote(),
                v.getDosagem(),
                v.getDataAplicacao().format(DATE_FORMATTER),
                v.getResponsavelAplicacao()
            };
            modeloTabela.addRow(linha);
        }
    }

    /**
     * Remove todas as linhas da tabela de vacinas.
     */
    private void limparTabela() {
        modeloTabela.setRowCount(0);
    }

    /**
     * Adiciona comportamento de placeholder ao campo de texto informado.
     *
     * <p>O placeholder é exibido quando o campo está vazio e na cor definida em
     * {@link Cores#COR_PLACEHOLDER}. Ao receber foco, o texto é removido automaticamente.</p>
     *
     * @param textField   campo de texto alvo
     * @param placeholder texto de dica a ser exibido quando vazio
     */
    private void addPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Cores.COR_PLACEHOLDER);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Cores.COR_RODAPE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Cores.COR_PLACEHOLDER);
                }
            }
        });
    }

    /**
     * Redefine o campo de pesquisa para o estado inicial (com placeholder) e limpa a tabela.
     * Chamado automaticamente quando a tela se torna visível.
     */
    private void limparCamposPesquisa() {
        if (txtCpfBusca != null) {
            txtCpfBusca.setText("");
            addPlaceholder(txtCpfBusca, "Digite o CPF do paciente");
        }
        limparTabela();
    }

    /**
     * Abre o formulário de cadastro de vacina ({@link TelaCadastroVacina}).
     * Limpa os campos do formulário antes de exibi-lo para evitar dados residuais.
     */
    private void abrirCadastroVacina() {
        telaCadastroVacina.limparCamposAoAbrir();
        telaCadastroVacina.setVisible(true);
    }

    /**
     * Atualiza a tabela após o cadastro bem-sucedido de uma vacina.
     *
     * <p>Chamado por {@link TelaCadastroVacina} para refletir automaticamente o novo
     * registro sem que o usuário precise pesquisar novamente.</p>
     *
     * @param paciente paciente que acabou de ter uma vacina registrada
     */
    public void atualizarTabelaAposCadastro(Paciente paciente) {
        if (paciente != null) {
            pacienteAtual = paciente;
            txtCpfBusca.setText(CPFUtils.formatarCPF(paciente.getCpf()));
            txtCpfBusca.setForeground(Cores.COR_RODAPE);
            atualizarTabelaVacinas();
        }
    }
}
