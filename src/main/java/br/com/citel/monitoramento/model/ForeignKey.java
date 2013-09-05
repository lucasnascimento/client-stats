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
@EqualsAndHashCode(of = { "foreingKeyDescription" })
public @Data
class ForeignKey implements Serializable{
	private static final long serialVersionUID = 5598054998394480757L;
	private String foreingKeyDescription;
}
