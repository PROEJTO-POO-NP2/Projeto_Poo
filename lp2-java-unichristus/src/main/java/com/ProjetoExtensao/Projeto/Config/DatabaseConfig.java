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
            if (responsavelRepositorio.findByEmail("jose.medico@recanto.org").isEmpty()) {
                System.out.println("Inserindo nova equipe de profissionais de saúde no banco de dados...");
                
                ResponsavelSaude rs1 = new ResponsavelSaude("jose.medico@recanto.org", "senha123", "José");
                rs1.setEspecialidade("Clínico Geral"); rs1.setCargo("Médico"); rs1.setRegistroProfissional("CRM 11111");
                responsavelRepositorio.save(rs1);
                
                ResponsavelSaude rs2 = new ResponsavelSaude("alisson.enf@recanto.org", "senha123", "Alisson");
                rs2.setEspecialidade("Enfermeiro Padrão"); rs2.setCargo("Enfermeiro"); rs2.setRegistroProfissional("COREN 22222");
                responsavelRepositorio.save(rs2);
                
                ResponsavelSaude rs3 = new ResponsavelSaude("esdras.tecnico@recanto.org", "senha123", "Esdras");
                rs3.setEspecialidade("Técnico em Enfermagem"); rs3.setCargo("Técnico"); rs3.setRegistroProfissional("COREN 33333");
                responsavelRepositorio.save(rs3);
                
                ResponsavelSaude rs4 = new ResponsavelSaude("vini.fisio@recanto.org", "senha123", "Vini");
                rs4.setEspecialidade("Fisioterapia Respiratória"); rs4.setCargo("Fisioterapeuta"); rs4.setRegistroProfissional("CREFITO 44444");
                responsavelRepositorio.save(rs4);
                
                ResponsavelSaude rs5 = new ResponsavelSaude("arthur.admin@recanto.org", "senha123", "Arthur");
                rs5.setEspecialidade("Administração Hospitalar"); rs5.setCargo("Administrador"); rs5.setRegistroProfissional("CRA 55555");
                responsavelRepositorio.save(rs5);
            }

            /*
             * Verifica se o banco já possui pacientes e consultas.
             */
            if (pacienteRepositorio.count() == 0) {
                System.out.println("Preenchendo pacientes e consultas de exemplo...");

                // Recupera os profissionais recém criados (ou já existentes)
                ResponsavelSaude rs1 = responsavelRepositorio.findByEmail("jose.medico@recanto.org").get();
                ResponsavelSaude rs2 = responsavelRepositorio.findByEmail("alisson.enf@recanto.org").get();
                ResponsavelSaude rs3 = responsavelRepositorio.findByEmail("esdras.tecnico@recanto.org").get();
                ResponsavelSaude rs4 = responsavelRepositorio.findByEmail("vini.fisio@recanto.org").get();
                ResponsavelSaude rs5 = responsavelRepositorio.findByEmail("arthur.admin@recanto.org").get();

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

                System.out.println("Preenchimento de pacientes e consultas concluído.");
            } else {
                System.out.println("Banco de dados já possui pacientes. Seed de pacientes ignorado.");
            }
        };
    }
}
