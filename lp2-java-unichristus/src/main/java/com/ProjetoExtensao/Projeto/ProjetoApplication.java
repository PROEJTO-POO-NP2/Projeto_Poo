package com.ProjetoExtensao.Projeto;

import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Classe principal responsável por inicializar a aplicação Spring Boot.
 *
 * Configura o carregamento seguro de variáveis de ambiente (.env) e
 * inicializa o contexto visual (Swing) e a navegação inicial.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@SpringBootApplication
public class ProjetoApplication {

	/**
	 * Método de entrada da aplicação.
	 *
	 * @param args argumentos de linha de comando
	 */
	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");

		// Lógica robusta para carregar o .env independente de onde a aplicação foi iniciada (raiz ou subpasta)
		java.io.File envLocal = new java.io.File(".env");
		java.io.File envSubpasta = new java.io.File("lp2-java-unichristus/.env");
		String envDir = null;

		if (envLocal.exists()) {
			envDir = "./";
		} else if (envSubpasta.exists()) {
			envDir = "./lp2-java-unichristus";
		}

		if (envDir != null) {
			Dotenv dotenv = Dotenv.configure().directory(envDir).load();
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
			System.out.println("=========================================================");
			System.out.println("[INFO] Arquivo .env carregado com sucesso do diretorio: " + (envDir.equals("./") ? "raiz atual" : envDir));
			System.out.println("[INFO] Host do banco configurado para: " + System.getProperty("DB_HOST", "localhost"));
			System.out.println("=========================================================");
		} else {
			System.out.println("=========================================================");
			System.out.println("[AVISO] Nenhum arquivo .env encontrado!");
			System.out.println("[AVISO] A aplicacao vai tentar usar localhost:5432 por padrao.");
			System.out.println("=========================================================");
		}

		ApplicationContext context = SpringApplication.run(ProjetoApplication.class, args);

		NavigationService navigationService = context.getBean(NavigationService.class);
		navigationService.abrirTelaLogin();
	}
}
