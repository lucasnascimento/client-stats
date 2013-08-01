	package br.com.citel.monitoramento.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class CFGSIS {
	@Id
	private String SIS_CODCFG;
	private String SIS_CODEMP;
	private String SIS_VALCFG;
}