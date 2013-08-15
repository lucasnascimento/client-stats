package br.com.citel.monitoramento.repository.portal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.CONTMO;
import br.com.citel.monitoramento.entity.CONTMOPK;

/**
 * Classe de repositório usando o SpringFramework para facilidades de CRUD.
 * 
 * @author lucas
 * 
 */
@Repository
public interface ContmoPortalRepository extends JpaRepository<CONTMO, CONTMOPK> {
	/**
	 * Buscando CONTMO por Empresa fisica e CNPJ
	 * 
	 * @param empresaFisica
	 * @param cnpj
	 * @return
	 */
	@Query("select c from CONTMO c where c.EMPRESA_FISICA = ?1 and c.CNPJ = ?2")
	List<CONTMO> findByEmpresaFiscaAndCNPJ(Long empresaFisica, String cnpj);
}
