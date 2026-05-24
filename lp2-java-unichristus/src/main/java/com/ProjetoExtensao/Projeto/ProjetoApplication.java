package com.ProjetoExtensao.Projeto;

import com.ProjetoExtensao.Projeto.servicos.NavigationService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ProjetoApplication {

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");

		// Carregar variáveis do arquivo .env se ele existir na raiz do projeto
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		ApplicationContext context = SpringApplication.run(ProjetoApplication.class, args);

		NavigationService navigationService = context.getBean(NavigationService.class);
		navigationService.abrirTelaLogin();
	}
}
