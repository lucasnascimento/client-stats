package br.com.citel.monitoramento.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Data;

/**
 * Classe ORM responsável por mapear a entidade de banco de dados, com abordagem
 * minimalista usando LAMBOK e como nome da classe o proprio nome da entidade no
 * Banco de Dados.
 * 
 * @author lucas
 * 
 */
@Entity
@IdClass(CONTMOPK.class)
public @Data
class CONTMO {
	@Id
	private String TABELA;
	@Id
	private Date DIA;
	@Id
	private Long EMPRESA;
	@Id
	private Long EMPRESA_FISICA;
	@Id
	private String CNPJ;
	private Long QTD_REG;
}
