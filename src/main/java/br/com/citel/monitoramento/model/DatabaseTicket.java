package br.com.citel.monitoramento.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO Classe utilitária.
 * 
 * @author lucas
 * 
 */
@AllArgsConstructor
@Data
public class DatabaseTicket {
	private String tableName;
	private String subject;
}
