	package br.com.citel.monitoramento.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="CFGSIS")
public @Data class ConfigSistema {
	@Id
	@Column(name="SIS_CODCFG")
	private String codigoConfiguracao;
	@Column(name="SIS_CODEMP")
	private String codigoEmpresa;
	@Column(name="SIS_VALCFG")
	private String valorConfiguracao;

}