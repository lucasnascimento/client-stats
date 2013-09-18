package br.com.citel.monitoramento.repository.portal;

import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.CFGDTA;
import br.com.citel.monitoramento.entity.CFGDTAPK;
import br.com.citel.monitoramento.repository.CustomRepository;

/**
 * Classe de repositório usando o SpringFramework para facilidades de CRUD.
 * 
 * @author lucas
 * 
 */
@Repository
public interface CfgdtaRepository extends CustomRepository<CFGDTA, CFGDTAPK> {
}
