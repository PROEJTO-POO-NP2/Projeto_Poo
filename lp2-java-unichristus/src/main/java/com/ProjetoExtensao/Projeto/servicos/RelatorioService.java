package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.models.Prescricao;
import com.ProjetoExtensao.Projeto.models.Prontuario;
import com.ProjetoExtensao.Projeto.models.Vacina;
import com.ProjetoExtensao.Projeto.repositorios.ProntuarioRepository;
import com.ProjetoExtensao.Projeto.repositorios.RelatorioRepository;
import com.ProjetoExtensao.Projeto.utils.EventosOcorridos;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;
    private final PacienteService pacienteService;
    private final VacinaService vacinaService;
    private final EventoSentinelaService eventoSentinelaService;
    private final ProntuarioRepository prontuarioRepository;

    public List<Paciente> gerarRelatorioPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("As datas de início e fim não podem ser nulas.");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim.");
        }
        return relatorioRepository.findByDataEntradaBetween(inicio, fim);
    }

    public RelatorioIndividualDTO gerarRelatorioIndividual(String cpf) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        Paciente paciente = pacienteService.findPacienteByCpf(cpfLimpo);
        
        Optional<Prontuario> prontuarioOpt = prontuarioRepository.findByPaciente(paciente);
        List<Prescricao> prescricoes = prontuarioOpt.map(Prontuario::getPrescricoes).orElse(new ArrayList<>());
        List<Vacina> vacinas = vacinaService.findByPaciente(paciente);
        
        return new RelatorioIndividualDTO(paciente, prescricoes, vacinas);
    }

    public double calcularPercentualVacinacao(String nomeVacina) {
        return vacinaService.calcularPercentualVacinacao(nomeVacina);
    }

    public double calcularPercentualIncidentes() {
        List<Paciente> pacientesAtivos = pacienteService.findPacientesByAtivo(true);
        if (pacientesAtivos.isEmpty()) {
            return 0.0;
        }
        long pacientesComIncidente = pacientesAtivos.stream()
            .filter(p -> !eventoSentinelaService.findEventosByPaciente(p).isEmpty())
            .count();
        return ((double) pacientesComIncidente / pacientesAtivos.size()) * 100.0;
    }

    public Map<EventosOcorridos, Long> contarEventosPorTipo() {
        return eventoSentinelaService.findAllEventos().stream()
            .collect(Collectors.groupingBy(
                com.ProjetoExtensao.Projeto.models.EventoSentinela::getEventosOcorridos,
                Collectors.counting()
            ));
    }
}