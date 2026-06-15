package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Vacina;
import com.ProjetoExtensao.Projeto.repositorios.VacinaRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VacinaService {
    
    private final VacinaRepositorio vacinaRepositorio;
    private final PacienteService pacienteService;

    public void salvar(Vacina vacina) {
        vacinaRepositorio.save(vacina);
    }

    public List<Vacina> findByPaciente(Paciente paciente) {
        return vacinaRepositorio.findByPaciente(paciente);
    }

    public List<Vacina> findByPacienteOrderByDataAplicacaoDesc(Paciente paciente) {
        return vacinaRepositorio.findByPacienteOrderByDataAplicacaoDesc(paciente);
    }

    public List<Vacina> findAll() {
        return vacinaRepositorio.findAll();
    }

    public double calcularPercentualVacinacao(String nomeVacina) {
        long totalAtivos = pacienteService.findPacientesByAtivo(true).size();
        if (totalAtivos == 0) {
            return 0.0;
        }
        long vacinadosAtivos = vacinaRepositorio.countByNomeVacinaIgnoreCaseAndPacienteAtivo(nomeVacina, true);
        return ((double) vacinadosAtivos / totalAtivos) * 100.0;
    }
}
