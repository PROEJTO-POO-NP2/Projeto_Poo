package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.Paciente;
import com.ProjetoExtensao.Projeto.repositorios.PacienteRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pelas regras de negócio relacionadas aos Pacientes.
 * 
 * Gerencia a comunicação entre os controladores/telas e o repositório de pacientes.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Service
@AllArgsConstructor
public class PacienteService {
    private PacienteRepositorio pacienteRepositorio;

    /**
     * Salva um novo paciente no banco de dados.
     * @param paciente paciente a ser salvo
     */
    public void salvarPaciente(Paciente paciente) {
        pacienteRepositorio.save(paciente);
    }

    /**
     * Busca pacientes por parte do nome (ignorando case).
     * @param nome string de busca
     * @return lista de pacientes encontrados
     */
    public List<Paciente> findPacientesByNomeCompleto(String nome) {
        return pacienteRepositorio.findByNomeCompletoContainingIgnoreCase(nome);
    }

    /**
     * Busca pacientes que contenham a string informada no CPF.
     * @param cpf string de busca
     * @return lista de pacientes encontrados
     */
    public List<Paciente> findPacientesByCpf(String cpf) {
        return pacienteRepositorio.findByCpfContaining(cpf);
    }

    /**
     * Recupera todos os pacientes cadastrados.
     * @return lista completa de pacientes
     */
    public List<Paciente> findAllPacientes() {
        return pacienteRepositorio.findAll();
    }

    /**
     * Busca um paciente exato pelo CPF.
     * @param cpf CPF do paciente
     * @return paciente encontrado
     * @throws RuntimeException se não for encontrado
     */
    public Paciente findPacienteByCpf(String cpf) {
        return pacienteRepositorio.findByCpf(cpf).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }

    /**
     * Busca pacientes ativos ou inativos.
     * @param ativo status de busca
     * @return lista correspondente
     */
    public List<Paciente> findPacientesByAtivo(Boolean ativo) {
        return pacienteRepositorio.findByAtivo(ativo);
    }

    /**
     * Busca um paciente pelo ID.
     * @param id identificador
     * @return paciente
     * @throws RuntimeException se não encontrado
     */
    public Paciente findPacienteById(Long id) {
        return pacienteRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
    }

    /**
     * Atualiza os dados de um paciente existente.
     * @param paciente paciente com dados atualizados
     */
    public void atualizarPaciente(Paciente paciente) {
        pacienteRepositorio.save(paciente);
    }
}
