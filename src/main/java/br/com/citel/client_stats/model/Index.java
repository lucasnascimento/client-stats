package br.com.citel.client_stats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(of = {"indexName"})
public @Data class Index {
	private String indexName;
	private String columnName;
}
