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
@EqualsAndHashCode(of = { "foreingKeyDescription" })
public @Data
class ForeignKey {
	private String foreingKeyDescription;
}
