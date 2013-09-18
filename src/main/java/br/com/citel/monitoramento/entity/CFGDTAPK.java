package br.com.citel.monitoramento.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe ORM responsável por mapear a PK da entidade de banco de dados, com
 * abordagem minimalista usando LAMBOK e como nome da classe o proprio nome da
 * entidade no Banco de Dados.
 * 
 * @author lucas
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
public @Data
class CFGDTAPK implements Serializable {
	private static final long serialVersionUID = -3162175232357637465L;
	private String DTA_VERSAO;
}
