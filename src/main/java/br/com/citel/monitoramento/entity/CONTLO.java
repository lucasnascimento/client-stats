package br.com.citel.monitoramento.entity;

import java.sql.Date;

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
@IdClass(CONTLOPK.class)
public @Data
class CONTLO {
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
