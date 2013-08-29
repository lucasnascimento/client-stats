package br.com.citel.monitoramento.repository.portal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.citel.monitoramento.entity.LOG_DTA;
import br.com.citel.monitoramento.entity.LOG_DTAPK;
import br.com.citel.monitoramento.repository.CustomRepository;

/**
 * Classe de repositório usando o SpringFramework para facilidades de CRUD.
 * 
 * @author lucas
 * 
 */
@Repository
public interface LogdataRepository extends CustomRepository<LOG_DTA, LOG_DTAPK> {
	/**
	 * Deletando todos os LOG_DTA do cnpj informado
	 * 
	 * @param cnpj
	 */
	@Modifying
	@Transactional(value="portalTM")
	@Query("delete from LOG_DTA l where l.LOG_C_G_C_ = ?1")
	void deleteByCNPJ(String cnpj);
}
