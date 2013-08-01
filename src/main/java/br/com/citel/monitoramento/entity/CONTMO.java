package br.com.citel.monitoramento.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class CONTMO {

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
