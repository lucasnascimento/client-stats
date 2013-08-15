package br.com.citel.monitoramento.entity;

import java.io.Serializable;
import java.util.Date;

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
class CONTMOPK implements Serializable {
	private static final long serialVersionUID = 3745520552492148903L;
	private String TABELA;
	private Date DIA;
	private Long EMPRESA;
	private Long EMPRESA_FISICA;
	private String CNPJ;
}
