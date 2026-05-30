package com.ProjetoExtensao.Projeto.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidade que representa um Paciente (idosa) no sistema.
 *
 * Contém dados pessoais como nome, CPF, data de nascimento,
 * nome da mãe, cartão SUS e data de entrada na instituição.
 * O campo {@code ativo} indica se o paciente está em acompanhamento.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Entity
@Table(name = "pacientes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(unique = true, nullable = false)
    private String cpf;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataNascimento;

    private String nomeMae;

    @Column(name = "cartao_sus", unique = true, nullable = false)
    private String cartaoSUS;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataEntrada;

    @Column(nullable = false)
    private Boolean ativo = true; // Por padrão, paciente é ativo

    /** Lista de consultas médicas realizadas pelo paciente. */
    @OneToMany(mappedBy = "paciente")
    private List<Consulta> consultas = new ArrayList<>();

    public Paciente(String nomeCompleto, String cpf, LocalDate dataNascimento, String nomeMae, String cartaoSUS, LocalDate dataEntrada) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.nomeMae = nomeMae;
        this.cartaoSUS = cartaoSUS;
        this.dataEntrada = dataEntrada;
        this.ativo = true; // Por padrão, paciente é ativo
        this.consultas = new ArrayList<>();
    }

    public void addConsulta(Consulta consulta) {
        consultas.add(consulta);
    }
}
