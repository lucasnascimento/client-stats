package br.com.citel.client_stats.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor

@Data
public class DatabaseTicket {
	private String tableName;
	private String subject;
}
