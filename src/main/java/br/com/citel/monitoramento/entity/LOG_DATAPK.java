package br.com.citel.monitoramento.entity;

import java.io.Serializable;

import lombok.Data;

public @Data
class LOG_DATAPK implements Serializable {
	private static final long serialVersionUID = 4875895808548080764L;
	private String LOG_C_G_C_;
	private String LOG_TABELA;
	private String LOG_MENSAGEM;
}
