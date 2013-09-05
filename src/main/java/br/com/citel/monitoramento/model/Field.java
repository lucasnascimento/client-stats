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
@EqualsAndHashCode(of = { "fieldName" })
public @Data
class Field implements Serializable {
	private static final long serialVersionUID = 2710410097602206096L;
	private String fieldName;
	private String type;
	private Boolean nullable;
	private String key;
	private String def;
	private String extra;
	private Boolean temCharcterSet;
}
