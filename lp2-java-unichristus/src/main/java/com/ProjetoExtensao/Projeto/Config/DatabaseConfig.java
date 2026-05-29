package com.ProjetoExtensao.Projeto.Config;

import com.ProjetoExtensao.Projeto.models.Consulta;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.ResponsavelSaude;
import com.ProjetoExtensao.Projeto.repositorios.ConsultaRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.PacienteRepositorio;
import com.ProjetoExtensao.Projeto.repositorios.ResponsavelRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Configuração de inicialização do banco de dados.
 *
 * Esta classe é responsável por popular o banco de dados com dados iniciais
 * (seed) na primeira execução da aplicação, utilizando o mecanismo
 * {@link CommandLineRunner} do Spring Boot.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Configuration
public class DatabaseConfig {

    /**
     * Popula o banco de dados com dados de exemplo caso as tabelas estejam vazias.
     *
     * Cria 5 profissionais de saúde (equipe atual), 10 pacientes e 5 consultas
     * de demonstração. A verificação é feita diretamente via repositório JPA,
     * garantindo compatibilidade com qualquer estado inicial do banco.
     *
     * @param responsavelRepositorio repositório de profissionais de saúde
     * @param pacienteRepositorio    repositório de pacientes
     * @param consultaRepositorio    repositório de consultas
     * @return CommandLineRunner que executa o seed na inicialização
     */
    @Bean
    CommandLineRunner initDB(ResponsavelRepositorio responsavelRepositorio,
                             PacienteRepositorio pacienteRepositorio,
                             ConsultaRepositorio consultaRepositorio) {
        return args -> {
            /*
             * Verifica se o banco já possui dados usando o repositório JPA
             * ao invés de query SQL direta, evitando problemas de schema
             * ou tabela inexistente na primeira execução.
             */
            if (pacienteRepositorio.count() == 0) {
                System.out.println("Preenchendo o banco de dados...");

                // === Profissionais de Saúde (Equipe Atual) ===
                ResponsavelSaude rs1 = responsavelRepositorio.save(
                        new ResponsavelSaude("jose.medico@recanto.org", "senha123", "José"));
                ResponsavelSaude rs2 = responsavelRepositorio.save(
                        new ResponsavelSaude("alisson.enf@recanto.org", "senha123", "Alisson"));
                ResponsavelSaude rs3 = responsavelRepositorio.save(
                        new ResponsavelSaude("esdras.tecnico@recanto.org", "senha123", "Esdras"));
                ResponsavelSaude rs4 = responsavelRepositorio.save(
                        new ResponsavelSaude("vini.fisio@recanto.org", "senha123", "Vini"));
                ResponsavelSaude rs5 = responsavelRepositorio.save(
                        new ResponsavelSaude("arthur.admin@recanto.org", "senha123", "Arthur"));

                // === Pacientes de Exemplo ===
                Paciente p1 = pacienteRepositorio.save(new Paciente("Ana Beatriz Silva", "12345678901",
                        LocalDate.of(1995, 3, 12), "Maria da Silva", "704030195830001", LocalDate.of(2024, 6, 10)));
                Paciente p2 = pacienteRepositorio.save(new Paciente("João Pedro Lima", "23456789012",
                        LocalDate.of(1988, 7, 23), "Fernanda Lima", "209384750192837", LocalDate.of(2024, 6, 11)));
                Paciente p3 = pacienteRepositorio.save(new Paciente("Mariana Costa", "34567890123",
                        LocalDate.of(2000, 1, 5), "Tatiane Costa", "807364950123845", LocalDate.of(2024, 6, 12)));
                Paciente p4 = pacienteRepositorio.save(new Paciente("Carlos Eduardo Rocha", "45678901234",
                        LocalDate.of(1972, 11, 30), "Elaine Rocha", "906573820194857", LocalDate.of(2024, 6, 13)));
                Paciente p5 = pacienteRepositorio.save(new Paciente("Juliana Martins", "56789012345",
                        LocalDate.of(1999, 9, 18), "Sandra Martins", "102938475601938", LocalDate.of(2024, 6, 14)));
                Paciente p6 = pacienteRepositorio.save(new Paciente("Felipe Almeida", "67890123456",
                        LocalDate.of(1985, 4, 9), "Luciana Almeida", "384756102938475", LocalDate.of(2024, 6, 15)));
                Paciente p7 = pacienteRepositorio.save(new Paciente("Larissa Oliveira", "78901234567",
                        LocalDate.of(1992, 2, 20), "Rosana Oliveira", "837465928374659", LocalDate.of(2024, 6, 15)));
                Paciente p8 = pacienteRepositorio.save(new Paciente("Vinícius Souza", "89012345678",
                        LocalDate.of(2003, 12, 1), "Patrícia Souza", "564738291028374", LocalDate.of(2024, 6, 15)));
                Paciente p9 = pacienteRepositorio.save(new Paciente("Camila Ferreira", "90123456789",
                        LocalDate.of(1990, 8, 14), "Sônia Ferreira", "908172635465738", LocalDate.of(2024, 6, 15)));
                Paciente p10 = pacienteRepositorio.save(new Paciente("Gabriel Mendes", "01234567890",
                        LocalDate.of(1997, 5, 25), "Adriana Mendes", "284756918273645", LocalDate.of(2024, 6, 15)));

                // === Consultas de Exemplo ===
                consultaRepositorio.save(new Consulta(LocalDate.now(), LocalTime.of(10, 0), "ROTINA", rs1, p1));
                consultaRepositorio.save(new Consulta(LocalDate.now(), LocalTime.of(12, 0), "ROTINA", rs2, p2));
                consultaRepositorio.save(new Consulta(LocalDate.now(), LocalTime.of(8, 30), "ESPECIALIZADA", rs3, p4));
                consultaRepositorio.save(new Consulta(LocalDate.now(), LocalTime.of(14, 20), "ESPECIALIZADA", rs4, p8));
                consultaRepositorio.save(new Consulta(LocalDate.now(), LocalTime.of(9, 0), "EMERGENCIAL", rs5, p9));

                System.out.println("Preenchimento do banco de dados concluído.");
            } else {
                System.out.println("Banco de dados já possui dados. Seed ignorado.");
            }
        };
    }
}
