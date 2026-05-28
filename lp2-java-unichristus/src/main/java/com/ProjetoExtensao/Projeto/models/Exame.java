package com.ProjetoExtensao.Projeto.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidade que representa um Exame médico solicitado ou realizado.
 *
 * Um exame está sempre vinculado a um {@link Prontuario} e contém
 * informações como nome do exame, data e resultado.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Entity
@Table(name = "exames")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Exame {

    /** Identificador único do exame. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Prontuário ao qual este exame está vinculado. */
    @ManyToOne
    @JoinColumn(name = "prontuario_id", nullable = false)
    private Prontuario prontuario;

    /** Nome/tipo do exame realizado (ex: Hemograma, Raio-X, etc.). */
    @Column(nullable = false)
    private String nomeExame;

    /** Data em que o exame foi solicitado ou realizado. */
    @Column(nullable = false)
    private LocalDate dataExame;

    /** Resultado do exame. Pode ser nulo enquanto aguarda resultado. */
    @Column(columnDefinition = "TEXT")
    private String resultado;

    /**
     * Construtor para criação de um novo exame.
     *
     * @param prontuario prontuário vinculado
     * @param nomeExame  nome/tipo do exame
     * @param dataExame  data do exame
     * @param resultado  resultado (pode ser nulo)
     */
    public Exame(Prontuario prontuario, String nomeExame, LocalDate dataExame, String resultado) {
        this.prontuario = prontuario;
        this.nomeExame = nomeExame;
        this.dataExame = dataExame;
        this.resultado = resultado;
    }
}
