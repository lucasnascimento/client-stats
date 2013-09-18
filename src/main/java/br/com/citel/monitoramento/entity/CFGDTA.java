package br.com.citel.monitoramento.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe ORM responsável por mapear a entidade de banco de dados, com abordagem
 * minimalista usando LAMBOK e como nome da classe o proprio nome da entidade no
 * Banco de Dados.
 * 
 * @author lucas
 * 
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(CFGDTAPK.class)
public @Data
class CFGDTA {
	@Id
	private String DTA_VERSAO;
	@Lob()
	private byte[] DTA_ESTRUTURA;
}
