package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.EventoSentinela;
import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.repositorios.EventoSentinelaRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável por gerenciar os Eventos Sentinelas (adversos).
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Service
@AllArgsConstructor
public class EventoSentinelaService {
    private EventoSentinelaRepositorio eventoSentinelaRepositorio;

    /**
     * Salva um evento sentinela no banco de dados.
     * @param evento objeto a ser salvo
     */
    public void salvarEvento(EventoSentinela evento) {
        eventoSentinelaRepositorio.save(evento);
    }

    /**
     * Lista todos os eventos associados a um paciente.
     * @param paciente paciente alvo
     * @return lista ordenada de eventos
     */
    public List<EventoSentinela> findEventosByPaciente(Paciente paciente) {
        return eventoSentinelaRepositorio.findByPacienteOrderByDataEventoDesc(paciente);
    }

    /**
     * Retorna a lista de todos os eventos sentinelas registrados.
     * @return lista de eventos
     */
    public List<EventoSentinela> findAllEventos() {
        return eventoSentinelaRepositorio.findAll();
    }

    /**
     * Busca um evento específico pelo ID.
     * @param id identificador
     * @return o evento
     */
    public EventoSentinela findEventoById(Long id) {
        return eventoSentinelaRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Evento não encontrado"));
    }
}
