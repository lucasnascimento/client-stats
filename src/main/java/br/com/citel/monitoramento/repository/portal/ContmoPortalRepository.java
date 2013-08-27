package br.com.citel.monitoramento.repository.portal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.CONTMO;
import br.com.citel.monitoramento.entity.CONTMOPK;
import br.com.citel.monitoramento.repository.CustomRepository;

/**
 * Classe de repositório usando o SpringFramework para facilidades de CRUD.
 * 
 * @author lucas
 * 
 */
@Repository
public interface ContmoPortalRepository extends CustomRepository<CONTMO, CONTMOPK> {
	/**
	 * Deletando CONTMO por Empresa fisica e CNPJ
	 * 
	 * @param empresaFisica
	 * @param cnpj
	 * @return
	 */
	@Modifying
	@Query("delete from CONTMO c where c.EMPRESA_FISICA = ?1 and c.CNPJ = ?2")
	void deleteByEmpresaFiscaAndCNPJ(Long empresaFisica, String cnpj);
}
