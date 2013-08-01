package br.com.citel.monitoramento.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class LOG_HD implements Serializable{
	private static final long serialVersionUID = -3057898700975787177L;
	@Id
	private String LOG_SISARQ;
	@Id
	private String LOG_C_G_C_;
	private String LOG_VERSAO;
	private String LOG_TAMANH;
	private String LOG_USADO_;
	private String LOG_DISPON;
	private Long LOG_PERUSO;
	private String LOG_MONTAG;
}
