package br.com.citel.monitoramento.entity;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Data;

@Entity
@IdClass(LOGBKPPK.class)
public @Data
class LOGBKP {
	@Id
	private String LOG_C_G_C_;
	@Id
	private String LOG_NOMBKP;
	private Date LOG_DTABKP;
	private Time LOG_HORBKP;
	private Long LOG_TAMBKP;
}