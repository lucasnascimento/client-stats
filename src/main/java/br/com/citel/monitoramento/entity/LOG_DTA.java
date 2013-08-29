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
@IdClass(LOG_DTAPK.class)
public @Data
class LOG_DTA {
	@Id
	private String LOG_C_G_C_;
	@Id
	private String LOG_TABELA;
	@Id
	private String LOG_MENSAGEM;
}
