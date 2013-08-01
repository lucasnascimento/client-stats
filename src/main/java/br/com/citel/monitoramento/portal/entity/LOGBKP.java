package br.com.citel.monitoramento.portal.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
public @Data class LOGBKP implements Serializable {

	private static final long serialVersionUID = -4632699633267310983L;

	@Id
	private String LOG_C_G_C_;
	@Id
	private String LOG_NOMBKP;
	
	private Date LOG_DTABKP;
	private Time LOG_HORBKP;
	private Long LOG_TAMBKP;
	
}
