package br.com.citel.monitoramento.repository.portal;

import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.LOGBKP;
import br.com.citel.monitoramento.entity.LOGBKPPK;
import br.com.citel.monitoramento.repository.CustomRepository;

/**
 * Classe de repositório usando o SpringFramework para facilidades de CRUD.
 * 
 * @author lucas
 * 
 */
@Repository
public interface LogbkpRepository extends CustomRepository<LOGBKP, LOGBKPPK> {
}
