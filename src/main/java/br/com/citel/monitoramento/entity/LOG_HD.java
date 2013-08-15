package br.com.citel.monitoramento.entity;

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
@IdClass(LOG_HDPK.class)
public @Data
class LOG_HD {
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
