package br.com.citel.monitoramento.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.CONTMO;
import br.com.citel.monitoramento.entity.CONTMOPK;

@Repository
public interface ContmoPortalRepository extends JpaRepository<CONTMO, CONTMOPK> {
	@Query("select c from CONTMO c where c.EMPRESA_FISICA = ?1 and c.CNPJ = ?2")
	List<CONTMO> findByEmpresaFiscaAndCNPJ(Long empresaFisica, String cnpj);
}
