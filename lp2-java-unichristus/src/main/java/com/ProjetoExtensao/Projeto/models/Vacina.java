package com.ProjetoExtensao.Projeto.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Entidade JPA que representa um registro de vacinação de um residente.
 *
 * <p>Cada instância corresponde a uma aplicação de vacina realizada em um {@link Paciente}
 * da instituição de longa permanência. O relacionamento com o paciente é {@code @ManyToOne},
 * ou seja, um residente pode ter múltiplos registros de vacinas ao longo do tempo.</p>
 *
 * <p>A tabela correspondente no banco PostgreSQL é {@code vacinas}.</p>
 *
 * <h3>Campos obrigatórios (NOT NULL):</h3>
 * <ul>
 *   <li>{@code paciente} — residente que recebeu a vacina</li>
 *   <li>{@code nomeVacina} — nome do imunobiológico aplicado</li>
 *   <li>{@code dataAplicacao} — data em que a vacina foi administrada</li>
 * </ul>
 *
 * <p>Os demais campos ({@code fabricante}, {@code lote}, {@code dosagem}, {@code responsavelAplicacao})
 * são opcionais e complementam o registro para fins de rastreabilidade clínica.</p>
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 * @see VacinaService
 * @see VacinaRepositorio
 */
@Entity
@Table(name = "vacinas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Vacina {

    /** Identificador único gerado automaticamente pelo banco de dados (PK auto-incremento). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Paciente que recebeu esta vacinação.
     * Mapeado como chave estrangeira {@code paciente_id} na tabela {@code vacinas}.
     */
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    /** Nome do imunobiológico aplicado (ex: "Gripe", "COVID-19 Pfizer", "Pneumocócica"). */
    @Column(nullable = false, length = 100)
    private String nomeVacina;

    /** Laboratório ou fabricante da vacina (opcional, ex: "Butantan", "Pfizer"). */
    @Column(length = 100)
    private String fabricante;

    /** Número de lote do frasco utilizado, para rastreabilidade farmacêutica. */
    @Column(length = 50)
    private String lote;

    /**
     * Data em que a vacina foi aplicada.
     * Formatada como {@code dd/MM/yyyy} para exibição em formulários HTML/Swing.
     */
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataAplicacao;

    /** Dose administrada (ex: "1ª dose", "2ª dose", "Reforço", "0.5 mL"). */
    @Column(length = 50)
    private String dosagem;

    /** Nome do profissional de saúde ou técnico que realizou a aplicação. */
    @Column(length = 100)
    private String responsavelAplicacao;

    /**
     * Construtor de conveniência para criação de registros vacinais sem necessidade de
     * informar o {@code id} (gerado automaticamente pelo banco).
     *
     * @param paciente               residente que recebeu a vacina
     * @param nomeVacina             nome do imunobiológico
     * @param fabricante             fabricante/laboratório (pode ser {@code null})
     * @param lote                   número do lote (pode ser {@code null})
     * @param dataAplicacao          data de aplicação (obrigatório)
     * @param dosagem                dose administrada (pode ser {@code null})
     * @param responsavelAplicacao   profissional responsável (pode ser {@code null})
     */
    public Vacina(Paciente paciente, String nomeVacina, String fabricante, String lote,
                  LocalDate dataAplicacao, String dosagem, String responsavelAplicacao) {
        this.paciente = paciente;
        this.nomeVacina = nomeVacina;
        this.fabricante = fabricante;
        this.lote = lote;
        this.dataAplicacao = dataAplicacao;
        this.dosagem = dosagem;
        this.responsavelAplicacao = responsavelAplicacao;
    }
}
