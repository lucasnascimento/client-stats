package br.com.citel.monitoramento.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data
class CONTLO implements Serializable {
	private static final long serialVersionUID = 6820327725672597892L;
	@Id
	private String LOG_C_G_C_;
	@Id
	private String LOG_DESCRI;
	@Id
	private String LOG_HORINI;
	private String LOG_VERSAO;
	private Date LOG_HORFIN;
	private String LOG_EXECUT;
}
