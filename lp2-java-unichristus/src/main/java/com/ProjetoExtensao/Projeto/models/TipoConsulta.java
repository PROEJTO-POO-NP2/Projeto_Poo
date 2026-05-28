package com.ProjetoExtensao.Projeto.models;

/**
 * Enum que define os tipos de consulta disponíveis no sistema.
 *
 * - ROTINA: consulta de acompanhamento regular
 * - EMERGENCIAL: atendimento de urgência
 * - ESPECIALIZADA: consulta com especialista
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
public enum TipoConsulta {
    ROTINA, EMERGENCIAL, ESPECIALIZADA;

    public static TipoConsulta getType(String type) {
        for (TipoConsulta consulta : TipoConsulta.values()) {
            if (consulta.toString().equalsIgnoreCase(type)) {
                return consulta;
            }
        }
        return null;
    }
}