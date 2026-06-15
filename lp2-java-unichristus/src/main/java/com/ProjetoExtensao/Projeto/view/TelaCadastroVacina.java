package com.ProjetoExtensao.Projeto.view;

import com.ProjetoExtensao.Projeto.infra.Cores;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Vacina;
import com.ProjetoExtensao.Projeto.servicos.PacienteService;
import com.ProjetoExtensao.Projeto.servicos.VacinaService;
import com.ProjetoExtensao.Projeto.utils.CPFUtils;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;

/**
 * Formulário modal para registro de novas vacinas de um residente.
 *
 * <p>Esta tela é aberta a partir de {@link TelaVacinas} e permite ao usuário:</p>
 * <ol>
 *   <li>Informar o CPF do residente e realizar a busca para confirmar o paciente.</li>
 *   <li>Preencher os dados da vacinação (nome, fabricante, lote, data, dosagem, responsável).</li>
 *   <li>Salvar o registro via {@link VacinaService#salvar(Vacina)}.</li>
 * </ol>
 *
 * <p>Após salvar com sucesso, notifica a {@link TelaVacinas} para atualizar a tabela de histórico.</p>
 *
 * <h3>Campos obrigatórios para salvar:</h3>
 * <ul>
 *   <li>CPF do paciente (paciente deve ser encontrado e carregado)</li>
 *   <li>Nome da vacina</li>
 *   <li>Data de aplicação (no formato dd/MM/yyyy)</li>
 * </ul>
 *
 * <p>A anotação {@code @Lazy} no {@code TelaVacinas} evita dependência circular entre os dois
 * beans Spring, que se referenciam mutuamente.</p>
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 * @see TelaVacinas
 * @see VacinaService
 */
@Component
@NoArgsConstructor
public class TelaCadastroVacina extends JFrame {

    // --- Dependências Spring ---

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private VacinaService vacinaService;

    /**
     * Referência lazy para a tela-pai. {@code @Lazy} é necessário para evitar
     * dependência circular: TelaVacinas → TelaCadastroVacina → TelaVacinas.
     */
    @Lazy
    @Autowired
    private TelaVacinas telaVacinas;

    // --- Componentes da UI ---

    /** Campo formatado para entrada do CPF (máscara: ###.###.###-##). */
    private JFormattedTextField txtCpf;

    /** Exibe o nome do paciente encontrado pelo CPF (somente leitura). */
    private JTextField txtNomePaciente;

    /** Nome do imunobiológico a ser registrado. */
    private JTextField txtNomeVacina;

    /** Fabricante/laboratório da vacina. */
    private JTextField txtFabricante;

    /** Número do lote do frasco utilizado. */
    private JTextField txtLote;

    /** Dose administrada (ex: "1ª dose", "0.5 mL"). */
    private JTextField txtDosagem;

    /** Campo formatado para a data de aplicação (máscara: ##/##/####). */
    private JFormattedTextField txtDataAplicacao;

    /** Nome do profissional responsável pela aplicação. */
    private JTextField txtResponsavel;

    /** Paciente carregado pela busca de CPF; obrigatório para salvar. */
    private Paciente pacienteAtual = null;

    /**
     * Inicializa e monta todos os componentes do formulário.
     *
     * <p>Chamado automaticamente pelo Spring após injeção das dependências.
     * Configura layout GridBag, campos mascarados, labels e botões de ação.</p>
     */
    @PostConstruct
    public void initUI() {
        setTitle("Registrar Vacina");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Limpa o formulário ao fechar a janela para evitar dados residuais
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                limparCampos();
            }
        });

        JPanel panelMain = new JPanel(new GridBagLayout());
        panelMain.setBackground(Cores.COR_FUNDO_CLARO);
        panelMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(8, 8, 8, 8);
        grid.fill = GridBagConstraints.HORIZONTAL;

        Font fonteLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fonteCampo = new Font("Segoe UI", Font.PLAIN, 13);

        // --- Campo: CPF do Paciente + botão Buscar ---
        grid.gridx = 0;
        grid.gridy = 0;
        panelMain.add(createLabel("CPF do Paciente:", fonteLabel), grid);

        grid.gridx = 1;
        JPanel panelCpf = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelCpf.setBackground(Cores.COR_FUNDO_CLARO);

        txtCpf = createFormattedTextField("###.###.###-##", fonteCampo);
        JButton btnBuscarCpf = new JButton("Buscar");
        btnBuscarCpf.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnBuscarCpf.setBackground(Cores.COR_RODAPE);
        btnBuscarCpf.setForeground(Color.WHITE);
        btnBuscarCpf.setFocusPainted(false);
        btnBuscarCpf.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBuscarCpf.addActionListener(e -> buscarPacientePorCpf());

        panelCpf.add(txtCpf);
        panelCpf.add(btnBuscarCpf);
        panelMain.add(panelCpf, grid);

        // --- Campo: Nome do Paciente (somente leitura) ---
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Nome do Paciente:", fonteLabel), grid);

        grid.gridx = 1;
        txtNomePaciente = new JTextField(20);
        txtNomePaciente.setFont(fonteCampo);
        txtNomePaciente.setEditable(false);
        txtNomePaciente.setBackground(new Color(240, 240, 240));
        panelMain.add(txtNomePaciente, grid);

        // --- Campo: Nome da Vacina ---
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Nome da Vacina:", fonteLabel), grid);

        grid.gridx = 1;
        txtNomeVacina = new JTextField(20);
        txtNomeVacina.setFont(fonteCampo);
        panelMain.add(txtNomeVacina, grid);

        // --- Campo: Fabricante ---
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Fabricante:", fonteLabel), grid);

        grid.gridx = 1;
        txtFabricante = new JTextField(20);
        txtFabricante.setFont(fonteCampo);
        panelMain.add(txtFabricante, grid);

        // --- Campo: Lote ---
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Lote:", fonteLabel), grid);

        grid.gridx = 1;
        txtLote = new JTextField(20);
        txtLote.setFont(fonteCampo);
        panelMain.add(txtLote, grid);

        // --- Campo: Dosagem ---
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Dosagem:", fonteLabel), grid);

        grid.gridx = 1;
        txtDosagem = new JTextField(20);
        txtDosagem.setFont(fonteCampo);
        panelMain.add(txtDosagem, grid);

        // --- Campo: Data de Aplicação (mascarado) ---
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Data de Aplicação:", fonteLabel), grid);

        grid.gridx = 1;
        txtDataAplicacao = createFormattedTextField("##/##/####", fonteCampo);
        panelMain.add(txtDataAplicacao, grid);

        // --- Campo: Responsável pela Aplicação ---
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Responsável Aplic.:", fonteLabel), grid);

        grid.gridx = 1;
        txtResponsavel = new JTextField(20);
        txtResponsavel.setFont(fonteCampo);
        panelMain.add(txtResponsavel, grid);

        // --- Botões de ação ---
        grid.gridx = 0;
        grid.gridy++;
        grid.gridwidth = 2;
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotoes.setBackground(Cores.COR_FUNDO_CLARO);

        JButton btnSalvar   = createButton("Salvar");
        JButton btnLimpar   = createButton("Limpar");
        JButton btnCancelar = createButton("Cancelar");

        btnSalvar.addActionListener(e -> salvarVacina());
        btnLimpar.addActionListener(e -> limparCampos());
        btnCancelar.addActionListener(e -> {
            limparCampos();
            dispose();
        });

        panelBotoes.add(btnSalvar);
        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnCancelar);

        panelMain.add(panelBotoes, grid);

        add(panelMain, BorderLayout.CENTER);
    }

    // =====================================================================
    // === Métodos de Fábrica de Componentes ==============================
    // =====================================================================

    /**
     * Cria um {@link JLabel} com a fonte especificada.
     *
     * @param text texto do label
     * @param font fonte a ser aplicada
     * @return JLabel configurado
     */
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    /**
     * Cria um {@link JFormattedTextField} com máscara aplicada e placeholder {@code '_'}.
     *
     * @param mask  string de máscara (ex: "###.###.###-##", "##/##/####")
     * @param font  fonte a ser aplicada ao campo
     * @return campo formatado, ou {@code null} se a máscara for inválida
     */
    private JFormattedTextField createFormattedTextField(String mask, Font font) {
        JFormattedTextField txt = null;
        try {
            MaskFormatter formatter = new MaskFormatter(mask);
            formatter.setPlaceholderCharacter('_');
            txt = new JFormattedTextField(formatter);
            txt.setColumns(15);
            txt.setFont(font);
        } catch (ParseException e) {
            System.err.println("Erro de parse na máscara: " + e.getMessage());
        }
        return txt;
    }

    /**
     * Cria um botão estilizado com as cores padrão do sistema.
     *
     * @param text rótulo do botão
     * @return JButton configurado com as cores e fonte padrão
     */
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Cores.COR_RODAPE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // =====================================================================
    // === Métodos de Negócio da Tela =====================================
    // =====================================================================

    /**
     * Busca o paciente pelo CPF digitado e exibe seu nome no campo de leitura.
     *
     * <p>Valida o tamanho do CPF antes de consultar o banco. Em caso de falha,
     * limpa o paciente atual e exibe diálogo de aviso.</p>
     */
    private void buscarPacientePorCpf() {
        String cpfLimpo = CPFUtils.limparCPF(txtCpf.getText());
        if (!CPFUtils.validarTamanhoCPF(cpfLimpo)) {
            JOptionPane.showMessageDialog(this, "Por favor, digite um CPF válido com 11 dígitos.", "CPF Inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            pacienteAtual = pacienteService.findPacienteByCpf(cpfLimpo);
            txtNomePaciente.setText(pacienteAtual.getNomeCompleto());
        } catch (RuntimeException e) {
            pacienteAtual = null;
            txtNomePaciente.setText("");
            JOptionPane.showMessageDialog(this, "Paciente não encontrado. Verifique o CPF digitado.", "Paciente Não Encontrado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Valida os campos, cria a entidade {@link Vacina} e persiste no banco.
     *
     * <p>Após salvar com sucesso:</p>
     * <ul>
     *   <li>Exibe mensagem de confirmação</li>
     *   <li>Notifica {@link TelaVacinas#atualizarTabelaAposCadastro(Paciente)}</li>
     *   <li>Limpa o formulário e fecha a janela</li>
     * </ul>
     */
    private void salvarVacina() {
        if (!validarCampos()) return;

        try {
            LocalDate dataAplicacao = converterData(txtDataAplicacao.getText());
            Vacina vacina = new Vacina(
                pacienteAtual,
                txtNomeVacina.getText().trim(),
                txtFabricante.getText().trim(),
                txtLote.getText().trim(),
                dataAplicacao,
                txtDosagem.getText().trim(),
                txtResponsavel.getText().trim()
            );

            vacinaService.salvar(vacina);

            JOptionPane.showMessageDialog(this, "✓ Vacina registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            telaVacinas.atualizarTabelaAposCadastro(pacienteAtual);
            limparCampos();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar vacina: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Valida as regras de negócio obrigatórias antes de salvar.
     *
     * <ul>
     *   <li>Paciente deve estar carregado (CPF buscado)</li>
     *   <li>Nome da vacina não pode estar em branco</li>
     *   <li>Data de aplicação deve estar completamente preenchida (sem {@code _})</li>
     * </ul>
     *
     * @return {@code true} se todos os campos obrigatórios são válidos; {@code false} caso contrário
     */
    private boolean validarCampos() {
        if (pacienteAtual == null) {
            JOptionPane.showMessageDialog(this, "Por favor, busque um paciente válido pelo CPF.", "Paciente Não Selecionado", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtNomeVacina.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, informe o nome da vacina.", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtDataAplicacao.getText().contains("_")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe a data de aplicação.", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Converte uma string de data no formato {@code dd/MM/yyyy} para {@link LocalDate}.
     *
     * @param dataStr string no formato "dd/MM/yyyy"
     * @return objeto LocalDate correspondente
     * @throws NumberFormatException se os componentes da data não forem numéricos
     */
    private LocalDate converterData(String dataStr) {
        String[] partes = dataStr.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int ano = Integer.parseInt(partes[2]);
        return LocalDate.of(ano, mes, dia);
    }

    /**
     * Redefine todos os campos do formulário para o estado inicial vazio.
     * Também limpa a referência ao {@link #pacienteAtual}.
     */
    public void limparCampos() {
        txtCpf.setText("");
        txtNomePaciente.setText("");
        txtNomeVacina.setText("");
        txtFabricante.setText("");
        txtLote.setText("");
        txtDosagem.setText("");
        txtDataAplicacao.setText("");
        txtResponsavel.setText("");
        pacienteAtual = null;
    }

    /**
     * Alias público para {@link #limparCampos()}, chamado por {@link TelaVacinas}
     * antes de tornar esta janela visível.
     */
    public void limparCamposAoAbrir() {
        limparCampos();
    }
}
