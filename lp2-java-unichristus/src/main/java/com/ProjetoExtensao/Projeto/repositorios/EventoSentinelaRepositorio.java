package com.ProjetoExtensao.Projeto.repositorios;

import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório para operações de CRUD da entidade {@link EventoSentinela}.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
public interface EventoSentinelaRepositorio extends JpaRepository<EventoSentinela, Long> {
    
    /**
     * Busca todos os eventos sentinelas de um paciente específico.
     * @param paciente o paciente
     * @return lista de eventos sentinelas do paciente
     */
    List<EventoSentinela> findByPaciente(Paciente paciente);
    
    /**
     * Busca todos os eventos sentinelas de um paciente ordenados pela data (mais recentes primeiro).
     * @param paciente o paciente
     * @return lista de eventos sentinelas ordenados
     */
    List<EventoSentinela> findByPacienteOrderByDataEventoDesc(Paciente paciente);
}
