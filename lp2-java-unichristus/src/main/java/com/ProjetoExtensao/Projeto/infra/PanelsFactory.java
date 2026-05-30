package com.ProjetoExtensao.Projeto.infra;

import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Fábrica de painéis para a interface gráfica (Header, Footer, Botões).
 *
 * Centraliza a criação dos painéis comuns a todas as telas,
 * garantindo consistência visual e reaproveitamento de código.
 * Gerencia também os botões padrão como Sair, Atualizar e Voltar ao Início.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Component
public class PanelsFactory {
    private IconManager iconManager;
    private NavigationService navigationService;

    private JButton refreshButton;
    private JButton adminButton;
    private JButton exitButton;

    public PanelsFactory(IconManager iconManager, @Lazy NavigationService navigationService) {
        this.iconManager = iconManager;
        this.navigationService = navigationService;
    }

    /**
     * Cria e retorna o painel de cabeçalho (Header).
     * @return o painel de cabeçalho
     */
    public JPanel getHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));

        ImageIcon logoIcon = iconManager.createScaledIcon("/images/logo.png", 50, 50);
        JLabel logoLabel = new JLabel(logoIcon);

        JPanel companyInfoPanel = new JPanel();
        companyInfoPanel.setLayout(new BoxLayout(companyInfoPanel, BoxLayout.Y_AXIS));
        companyInfoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("RECANTO DO SAGRADO CORAÇÃO");
        nameLabel.setForeground(Cores.COR_RODAPE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel subtitleLabel = new JLabel("ASSISTÊNCIA SOCIAL CATARINA LABOURÉ");
        subtitleLabel.setForeground(Color.LIGHT_GRAY);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        companyInfoPanel.add(nameLabel);
        companyInfoPanel.add(subtitleLabel);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(logoLabel);
        leftPanel.add(companyInfoPanel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        adminButton = new JButton("Administrador Painel", iconManager.createScaledIcon("/images/admin.png", 15, 15));
        exitButton = new JButton("Sair", iconManager.createScaledIcon("/images/exit.png", 15, 15));
        refreshButton = new JButton("Atualizar", iconManager.createScaledIcon("/images/refresh.png", 20, 20));

        for (JButton btn : new JButton[]{adminButton, exitButton, refreshButton}) {
            btn.setBackground(new Color(Cores.COR_RODAPE.getRGB()));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(new EmptyBorder(8, 12, 8, 12));
            if (btn == refreshButton) {
                btn.setBorder(new EmptyBorder(8, 8, 8, 8));
            }
        }

        exitButton.addActionListener(e -> {
            navigationService.abrirTelaLogin();

            Window janelaAtual = SwingUtilities.getWindowAncestor((java.awt.Component) e.getSource());
            if (janelaAtual != null) {
                janelaAtual.dispose();
            }
        });

        adminButton.addActionListener(e -> {
            navigationService.abrirTelaGeral();

            Window janelaAtual = SwingUtilities.getWindowAncestor((java.awt.Component) e.getSource());
            if (janelaAtual != null) {
                janelaAtual.dispose();
            }
        });


        buttonsPanel.add(adminButton);
        buttonsPanel.add(exitButton);
        buttonsPanel.add(refreshButton);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Retorna o botão de atualizar padrão.
     * @return o botão de refresh
     */
    public JButton getRefreshButton() {
        return refreshButton;
    }

    /**
     * Retorna o botão do painel administrativo.
     * @return o botão admin
     */
    public JButton getAdminButton(){
        return adminButton;
    }

    /**
     * Cria e retorna o painel de rodapé (Footer).
     * @return o painel de rodapé
     */
    public JPanel getFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(Cores.COR_RODAPE);
        // Sem chamada a getWidth() de JFrame, defino um tamanho padrão genérico se necessário, 
        // ou omito setPreferredSize se o layout tratar. O layout FlowLayout vai tratar bem o height.
        footerPanel.setPreferredSize(new Dimension(800, 400));
        footerPanel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, 40));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        JLabel dateTimeLabel = new JLabel(sdf.format(new Date()));
        dateTimeLabel.setForeground(Color.WHITE);
        dateTimeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateTimeLabel.setBorder(new EmptyBorder(0, 0, 0, 25));

        footerPanel.add(dateTimeLabel);

        return footerPanel;
    }
}
