package br.com.citel.monitoramento.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * Classe ORM responsável por mapear a PK da entidade de banco de dados, com
 * abordagem minimalista usando LAMBOK e como nome da classe o proprio nome da
 * entidade no Banco de Dados.
 * 
 * @author lucas
 * 
 */
public @Data
class LOG_DTAPK implements Serializable {
	private static final long serialVersionUID = 4875895808548080764L;
	private String LOG_C_G_C_;
	private String LOG_TABELA;
}
