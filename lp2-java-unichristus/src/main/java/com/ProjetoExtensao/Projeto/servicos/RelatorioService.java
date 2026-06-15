package com.ProjetoExtensao.Projeto.servicos; // <- ADICIONADO ".Projeto"

import com.ProjetoExtensao.Projeto.repositorios.RelatorioRepository; // <- ADICIONADO ".Projeto"
import com.ProjetoExtensao.Projeto.models.Paciente; // <- ADICIONADO ".Projeto"
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private RelatorioRepository relatorioRepository;

    public List<Paciente> gerarRelatorioPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("As datas de início e fim não podem ser nulas.");
        }
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim.");
        }
        
        // Chamando o método correto que criámos no repositório
        return relatorioRepository.findByDataEntradaBetween(inicio, fim);
    }
}