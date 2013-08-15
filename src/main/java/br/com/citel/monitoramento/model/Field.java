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
@EqualsAndHashCode(of = { "fieldName" })
public @Data
class Field {
	private String fieldName;
	private String type;
	private Boolean nullable;
	private String key;
	private String def;
	private String extra;
}
