package br.com.citel.monitoramento.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class CONTMO implements Serializable{
	private static final long serialVersionUID = 3745520552492148903L;
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
