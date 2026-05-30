package com.ProjetoExtensao.Projeto.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Entidade que representa um Profissional/Responsável de Saúde.
 *
 * Responsável por realizar consultas e atendimentos aos pacientes.
 * Contém dados de autenticação (email/senha) para login no sistema.
 *
 * Nota: As senhas são armazenadas em texto plano por se tratar de
 * um projeto acadêmico. Em produção, utilizar hash (BCrypt).
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Entity
@Table(name = "responsaveis_saude")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ResponsavelSaude {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String nomeCompleto;

    /** Lista de consultas realizadas por este profissional. */
    @OneToMany(mappedBy = "responsavelSaude")
    private List<Consulta> consultas = new ArrayList<>();

    /** Especialidade médica ou área de atuação (ex: Clinico Geral, Enfermeiro, Fisioterapeuta). */
    @Column(length = 100)
    private String especialidade;

    /** Número do registro no conselho profissional (ex: CRM, COREN, CREFITO). */
    @Column(length = 20)
    private String registroProfissional;

    /** Cargo que ocupa na instituição (ex: Medico, Enfermeiro, Tecnico de Enfermagem). */
    @Column(length = 50)
    private String cargo;

    public ResponsavelSaude(String email, String senha, String nomeCompleto) {
        this.email = email;
        this.senha = senha;
        this.nomeCompleto = nomeCompleto;
        this.consultas = new ArrayList<>();
    }

    public void addConsulta(Consulta consulta) {
        consultas.add(consulta);
    }
}
