package br.com.citel.monitoramento.model;

import java.io.Serializable;
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
class Table implements Serializable {
	private static final long serialVersionUID = 2031697677823305454L;
	private String tableName;
	private String tableNameAsIs;
	private String engine;
	private String collation;
	private List<Field> fields;
	private List<Index> indexes;
	private List<ForeignKey> foreignKeys;
	private String createTable;
}
