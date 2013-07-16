package br.com.citel.client_stats.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(of = {"tableName"})
public @Data class Table {
	private String tableName;
	private List<Field> fields;
	private List<Index> indexes;
}
