package br.com.citel.monitoramento.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Data;

@Entity
@IdClass(LOG_DTAPK.class)
public @Data
class LOG_DTA {
	@Id
	private String LOG_C_G_C_;
	private String LOG_TABELA;
	@Id
	private String LOG_MENSAGEM;
}
