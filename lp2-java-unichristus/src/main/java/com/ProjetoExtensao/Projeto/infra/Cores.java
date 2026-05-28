package com.ProjetoExtensao.Projeto.infra;

import java.awt.*;

/**
 * Classe utilitária que centraliza as cores do sistema.
 *
 * Todas as cores da interface gráfica devem ser referenciadas
 * a partir desta classe para manter a consistência visual.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
public class Cores {

    // === Cores de fundo ===
    /** Cor de fundo escuro (sidebar, header escuro). */
    public static final Color COR_FUNDO_ESCURO = new Color(0x333333);

    /** Cor de fundo claro (área principal das telas). */
    public static final Color COR_FUNDO_CLARO = new Color(0xF0F0F0);

    // === Cores institucionais ===
    /** Azul escuro institucional (rodapé, botões principais, títulos). */
    public static final Color COR_RODAPE = new Color(0x2A3A68);

    /** Cor padrão para texto de rótulos e títulos de seção. */
    public static final Color COR_LETRA_PAINEL = new Color(0x666666);

    // === Cores de status ===
    /** Vermelho para alertas e indicadores de atenção. */
    public static final Color COR_VERMELHO_IDOSAS = new Color(0xD9534F);

    /** Verde para indicadores positivos (enfermaria, sucesso). */
    public static final Color COR_VERDE_ENFERMARIA = new Color(0x5CB85C);

    // === Cores auxiliares para UI melhorada ===
    /** Azul claro para hover de botões e destaques suaves. */
    public static final Color COR_AZUL_HOVER = new Color(0x3A4F8C);

    /** Cinza claro para bordas de campos e separadores. */
    public static final Color COR_BORDA = new Color(0xCCCCCC);

    /** Branco para fundos de formulários e cards. */
    public static final Color COR_BRANCO = Color.WHITE;

    /** Cor de placeholder em campos de texto. */
    public static final Color COR_PLACEHOLDER = new Color(150, 150, 150);

    /** Amarelo suave para avisos/warnings. */
    public static final Color COR_AMARELO_AVISO = new Color(0xF0AD4E);

    /** Azul info para mensagens informativas. */
    public static final Color COR_AZUL_INFO = new Color(0x5BC0DE);
}