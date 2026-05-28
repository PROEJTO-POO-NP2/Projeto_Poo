package com.ProjetoExtensao.Projeto.servicos;

import com.ProjetoExtensao.Projeto.models.*;
import com.ProjetoExtensao.Projeto.repositorios.ExameRepository;
import com.ProjetoExtensao.Projeto.repositorios.PrescricaoRepository;
import com.ProjetoExtensao.Projeto.repositorios.ProntuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócios dos Prontuários Médicos.
 *
 * Gerencia operações de CRUD para prontuários, além de vincular
 * exames, prescrições e gerar resumos do histórico do paciente.
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Service
public class ProntuarioService {

    @Autowired
    private ProntuarioRepository prontuarioRepository;

    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private PrescricaoRepository prescricaoRepository;

    /**
     * Lista todos os prontuários cadastrados no sistema.
     *
     * @return lista de todos os prontuários
     */
    public List<Prontuario> listarTodos() {
        return prontuarioRepository.findAll();
    }

    /**
     * Busca um prontuário pelo seu ID.
     *
     * @param id identificador do prontuário
     * @return Optional contendo o prontuário, se encontrado
     */
    public Optional<Prontuario> buscarPorId(Long id) {
        return prontuarioRepository.findById(id);
    }

    /**
     * Busca o prontuário de um paciente específico.
     *
     * @param paciente paciente cujo prontuário será buscado
     * @return Optional contendo o prontuário, se encontrado
     */
    public Optional<Prontuario> buscarPorPaciente(Paciente paciente) {
        return prontuarioRepository.findByPaciente(paciente);
    }

    /**
     * Busca o prontuário pelo CPF do paciente.
     *
     * @param cpf CPF do paciente
     * @return Optional contendo o prontuário, se encontrado
     */
    public Optional<Prontuario> buscarPorCpf(String cpf) {
        return prontuarioRepository.findByPacienteCpf(cpf);
    }

    /**
     * Salva ou atualiza um prontuário no banco de dados.
     *
     * @param prontuario prontuário a ser salvo
     * @return prontuário salvo com ID gerado
     */
    public Prontuario salvarProntuario(Prontuario prontuario) {
        return prontuarioRepository.save(prontuario);
    }

    /**
     * Remove um prontuário pelo ID.
     *
     * @param id identificador do prontuário a ser removido
     */
    public void deletarProntuario(Long id) {
        prontuarioRepository.deleteById(id);
    }

    /**
     * Adiciona um exame ao prontuário especificado.
     *
     * @param prontuarioId ID do prontuário
     * @param exame        exame a ser adicionado
     * @return exame salvo com ID gerado
     * @throws RuntimeException se o prontuário não for encontrado
     */
    public Exame adicionarExame(Long prontuarioId, Exame exame) {
        Prontuario prontuario = prontuarioRepository.findById(prontuarioId)
                .orElseThrow(() -> new RuntimeException("Prontuário não encontrado."));
        prontuario.vincularExame(exame);
        return exameRepository.save(exame);
    }

    /**
     * Adiciona uma prescrição ao prontuário especificado.
     *
     * @param prontuarioId ID do prontuário
     * @param prescricao   prescrição a ser adicionada
     * @return prescrição salva com ID gerado
     * @throws RuntimeException se o prontuário não for encontrado
     */
    public Prescricao adicionarPrescricao(Long prontuarioId, Prescricao prescricao) {
        Prontuario prontuario = prontuarioRepository.findById(prontuarioId)
                .orElseThrow(() -> new RuntimeException("Prontuário não encontrado."));
        prontuario.addPrescricao(prescricao);
        return prescricaoRepository.save(prescricao);
    }

    /**
     * Gera o resumo completo do histórico do prontuário para relatórios.
     *
     * @param prontuarioId ID do prontuário
     * @return texto formatado com o resumo
     * @throws RuntimeException se o prontuário não for encontrado
     */
    public String gerarResumoHistorico(Long prontuarioId) {
        Prontuario prontuario = prontuarioRepository.findById(prontuarioId)
                .orElseThrow(() -> new RuntimeException("Prontuário não encontrado."));
        return prontuario.gerarResumo();
    }
}
