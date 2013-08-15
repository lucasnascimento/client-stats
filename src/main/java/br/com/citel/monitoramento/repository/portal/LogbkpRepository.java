package br.com.citel.monitoramento.repository.portal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.LOGBKP;
import br.com.citel.monitoramento.entity.LOGBKPPK;

/**
 * Classe de repositório usando o SpringFramework para facilidades de CRUD.
 * 
 * @author lucas
 * 
 */
@Repository
public interface LogbkpRepository extends CrudRepository<LOGBKP, LOGBKPPK> {
}
