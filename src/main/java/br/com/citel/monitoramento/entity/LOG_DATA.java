package br.com.citel.monitoramento.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class LOG_DATA {
	@Id
	private String LOG_C_G_C_;
	private String LOG_TABELA;
	private String LOG_MENSAGEM;
}
