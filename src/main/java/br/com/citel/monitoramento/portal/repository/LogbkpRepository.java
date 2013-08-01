package br.com.citel.monitoramento.portal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.LOGBKP;

@Repository
public interface LogbkpRepository extends CrudRepository<LOGBKP, Long> {
}
