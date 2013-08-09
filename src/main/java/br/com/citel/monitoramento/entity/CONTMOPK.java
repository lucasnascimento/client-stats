package br.com.citel.monitoramento.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

public @Data
class CONTMOPK implements Serializable {
	private static final long serialVersionUID = 3745520552492148903L;
	private String TABELA;
	private Date DIA;
	private Long EMPRESA;
	private Long EMPRESA_FISICA;
	private String CNPJ;
}
