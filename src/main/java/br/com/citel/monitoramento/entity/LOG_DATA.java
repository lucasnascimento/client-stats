package br.com.citel.monitoramento.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Data;

@Entity
@IdClass(LOG_DATAPK.class)
public @Data
class LOG_DATA {
	@Id
	private String LOG_C_G_C_;
	private String LOG_TABELA;
	@Id
	private String LOG_MENSAGEM;
}
