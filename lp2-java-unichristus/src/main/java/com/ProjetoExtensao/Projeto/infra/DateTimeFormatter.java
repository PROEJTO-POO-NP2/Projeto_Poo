package com.ProjetoExtensao.Projeto.infra;

import org.springframework.stereotype.Component;

/**
 * Utilitário de formatação de datas e horas padronizado para o sistema.
 *
 * Utilizado para garantir que todas as exibições e conversões de datas
 * sigam o formato brasileiro (dd/MM/yyyy e HH:mm).
 *
 * @author José, Alisson, Esdras, Vini, Arthur
 * @version 2.0
 */
@Component
public class DateTimeFormatter {
    public static final java.time.format.DateTimeFormatter DATE_TIME_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final java.time.format.DateTimeFormatter TIME_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("HH:mm");
}
