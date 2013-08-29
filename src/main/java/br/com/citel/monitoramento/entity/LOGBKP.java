package br.com.citel.monitoramento.entity;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Data;

/**
 * Classe ORM responsável por mapear a entidade de banco de dados, com abordagem
 * minimalista usando LAMBOK e como nome da classe o proprio nome da entidade no
 * Banco de Dados.
 * 
 * @author lucas
 * 
 */
@Entity
@IdClass(LOGBKPPK.class)
public @Data
class LOGBKP {
	@Id
	private String LOG_C_G_C_;
	@Id
	private Date LOG_DTABKP;
	@Id
	private String LOG_NOMBKP;
	private Time LOG_HORBKP;
	private Long LOG_TAMBKP;
}
