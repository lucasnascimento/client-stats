package br.com.citel.monitoramento.model;

import java.io.Serializable;

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
class Index implements Serializable {
	private static final long serialVersionUID = -7680150377133463507L;
	private String indexName;
	private String columnName;
	private Boolean unique;
}
