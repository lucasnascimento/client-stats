package br.com.citel.monitoramento.model;

import java.util.List;

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
@EqualsAndHashCode(of = { "tableName" })
public @Data
class Table {
	private String tableName;
	private String engine;
	private String collation;
	private List<Field> fields;
	private List<Index> indexes;
	private List<ForeignKey> foreignKeys;
}
