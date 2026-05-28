package com.ProjetoExtensao.Projeto.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidade que representa uma Prescrição médica.
 *
 * Contém informações sobre o medicamento prescrito, dosagem,
 * instruções de uso e data da prescrição. Está vinculada a
 * um {@link Prontuario}.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Entity
@Table(name = "prescricoes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Prescricao {

    /** Identificador único da prescrição. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Prontuário ao qual esta prescrição pertence. */
    @ManyToOne
    @JoinColumn(name = "prontuario_id", nullable = false)
    private Prontuario prontuario;

    /** Nome do medicamento prescrito. */
    @Column(nullable = false)
    private String medicamento;

    /** Dosagem do medicamento (ex: "500mg", "2 comprimidos"). */
    @Column(nullable = false)
    private String dosagem;

    /** Instruções de uso do medicamento (posologia, horários, etc.). */
    @Column(columnDefinition = "TEXT")
    private String instrucoes;

    /** Data em que a prescrição foi emitida. */
    @Column(nullable = false)
    private LocalDate dataPrescricao;

    /**
     * Construtor para criação de uma nova prescrição.
     *
     * @param prontuario     prontuário vinculado
     * @param medicamento    nome do medicamento
     * @param dosagem        dosagem prescrita
     * @param instrucoes     instruções de uso
     * @param dataPrescricao data da prescrição
     */
    public Prescricao(Prontuario prontuario, String medicamento, String dosagem,
                      String instrucoes, LocalDate dataPrescricao) {
        this.prontuario = prontuario;
        this.medicamento = medicamento;
        this.dosagem = dosagem;
        this.instrucoes = instrucoes;
        this.dataPrescricao = dataPrescricao;
    }
}
