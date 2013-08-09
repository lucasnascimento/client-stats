package br.com.citel.monitoramento.portal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.LOGBKP;
import br.com.citel.monitoramento.entity.LOGBKPPK;

@Repository
public interface LogbkpRepository extends CrudRepository<LOGBKP, LOGBKPPK> {
}
