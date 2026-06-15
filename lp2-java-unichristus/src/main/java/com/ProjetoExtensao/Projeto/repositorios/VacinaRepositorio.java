package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacinaRepositorio extends JpaRepository<Vacina, Long> {
    List<Vacina> findByPaciente(Paciente paciente);
    List<Vacina> findByNomeVacinaIgnoreCase(String nomeVacina);
    long countByNomeVacinaIgnoreCaseAndPacienteAtivo(String nomeVacina, Boolean ativo);
    List<Vacina> findByPacienteOrderByDataAplicacaoDesc(Paciente paciente);
}
