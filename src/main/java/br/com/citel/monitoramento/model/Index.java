package br.com.citel.monitoramento.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO Classe utilitária.
 * 
 * @author lucas
 * 
 */
@AllArgsConstructor
@EqualsAndHashCode(of = { "columnName" })
public @Data
class Index {
	private String indexName;
	private String columnName;
	private Boolean unique;
}
