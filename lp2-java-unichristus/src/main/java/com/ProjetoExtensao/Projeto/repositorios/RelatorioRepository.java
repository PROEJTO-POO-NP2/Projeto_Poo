package com.ProjetoExtensao.Projeto.repositorios; // <- AJUSTADO COM ".Projeto"

import com.ProjetoExtensao.Projeto.models.Paciente; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RelatorioRepository extends JpaRepository<Paciente, Long> {
    
    // ALTERADO PARA METER "DataEntrada" no nome do método para condizer com o modelo
    List<Paciente> findByDataEntradaBetween(LocalDate dataInicio, LocalDate dataFim);
}