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

@Component
@NoArgsConstructor
public class TelaCadastroVacina extends JFrame {
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private VacinaService vacinaService;
    
    @Lazy
    @Autowired
    private TelaVacinas telaVacinas;

    private JFormattedTextField txtCpf;
    private JTextField txtNomePaciente;
    private JTextField txtNomeVacina;
    private JTextField txtFabricante;
    private JTextField txtLote;
    private JTextField txtDosagem;
    private JFormattedTextField txtDataAplicacao;
    private JTextField txtResponsavel;
    private Paciente pacienteAtual = null;

    @PostConstruct
    public void initUI() {
        setTitle("Registrar Vacina");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
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

        // CPF do Paciente
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

        // Nome do Paciente (somente leitura)
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Nome do Paciente:", fonteLabel), grid);
        
        grid.gridx = 1;
        txtNomePaciente = new JTextField(20);
        txtNomePaciente.setFont(fonteCampo);
        txtNomePaciente.setEditable(false);
        txtNomePaciente.setBackground(new Color(240, 240, 240));
        panelMain.add(txtNomePaciente, grid);

        // Nome da Vacina
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Nome da Vacina:", fonteLabel), grid);
        
        grid.gridx = 1;
        txtNomeVacina = new JTextField(20);
        txtNomeVacina.setFont(fonteCampo);
        panelMain.add(txtNomeVacina, grid);

        // Fabricante
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Fabricante:", fonteLabel), grid);
        
        grid.gridx = 1;
        txtFabricante = new JTextField(20);
        txtFabricante.setFont(fonteCampo);
        panelMain.add(txtFabricante, grid);

        // Lote
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Lote:", fonteLabel), grid);
        
        grid.gridx = 1;
        txtLote = new JTextField(20);
        txtLote.setFont(fonteCampo);
        panelMain.add(txtLote, grid);

        // Dosagem
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Dosagem:", fonteLabel), grid);
        
        grid.gridx = 1;
        txtDosagem = new JTextField(20);
        txtDosagem.setFont(fonteCampo);
        panelMain.add(txtDosagem, grid);

        // Data de Aplicação
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Data de Aplicação:", fonteLabel), grid);
        
        grid.gridx = 1;
        txtDataAplicacao = createFormattedTextField("##/##/####", fonteCampo);
        panelMain.add(txtDataAplicacao, grid);

        // Responsável pela Aplicação
        grid.gridx = 0;
        grid.gridy++;
        panelMain.add(createLabel("Responsável Aplic.:", fonteLabel), grid);
        
        grid.gridx = 1;
        txtResponsavel = new JTextField(20);
        txtResponsavel.setFont(fonteCampo);
        panelMain.add(txtResponsavel, grid);

        // Botões
        grid.gridx = 0;
        grid.gridy++;
        grid.gridwidth = 2;
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotoes.setBackground(Cores.COR_FUNDO_CLARO);
        
        JButton btnSalvar = createButton("Salvar");
        JButton btnLimpar = createButton("Limpar");
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

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

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

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Cores.COR_RODAPE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

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

    private LocalDate converterData(String dataStr) {
        String[] partes = dataStr.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int ano = Integer.parseInt(partes[2]);
        return LocalDate.of(ano, mes, dia);
    }

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

    public void limparCamposAoAbrir() {
        limparCampos();
    }
}
