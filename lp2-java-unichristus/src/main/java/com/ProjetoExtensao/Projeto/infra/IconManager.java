package com.ProjetoExtensao.Projeto.infra;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Classe responsável por carregar e gerenciar ícones da interface gráfica.
 *
 * Busca os arquivos de imagem no classpath e permite escalonar imagens
 * para os tamanhos corretos nos botões e painéis.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Component
public class IconManager {
    public ImageIcon createIcon(String path) {
        URL url = IconManager.class.getResource(path);
        if (url == null) {
            System.err.println("Erro ao carregar o recurso: " + path);
            return null;
        }
        return new ImageIcon(url);
    }

    public ImageIcon createScaledIcon(String path, int width, int height) {
        ImageIcon originalIcon = createIcon(path);
        if (originalIcon == null) {
            return null;
        }
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
