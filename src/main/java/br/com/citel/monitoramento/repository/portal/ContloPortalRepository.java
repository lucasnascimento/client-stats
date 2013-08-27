package br.com.citel.monitoramento.repository.portal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.CONTLO;
import br.com.citel.monitoramento.entity.CONTLOPK;
import br.com.citel.monitoramento.repository.CustomRepository;

/**
 * Classe de repositório usando o SpringFramework para facilidades de CRUD.
 * 
 * @author lucas
 * 
 */
@Repository
public interface ContloPortalRepository extends CustomRepository<CONTLO, CONTLOPK> {
	
	/**
	 * Deletando os CONTLO por LOG_C_G_C_
	 * 
	 * @param cnpj
	 * @return
	 */
	@Modifying
	@Query("delete from CONTLO c where c.LOG_C_G_C_ = ?1 ")
	int deleteByCNPJ(String cnpj);
}
