package com.ProjetoExtensao.Projeto.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "vacinas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Vacina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(nullable = false, length = 100)
    private String nomeVacina;

    @Column(length = 100)
    private String fabricante;

    @Column(length = 50)
    private String lote;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate dataAplicacao;

    @Column(length = 50)
    private String dosagem;

    @Column(length = 100)
    private String responsavelAplicacao;

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
