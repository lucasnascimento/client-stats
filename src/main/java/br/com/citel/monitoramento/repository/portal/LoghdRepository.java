package br.com.citel.monitoramento.repository.portal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.LOG_HD;
import br.com.citel.monitoramento.entity.LOG_HDPK;

@Repository
public interface LoghdRepository extends CrudRepository<LOG_HD, LOG_HDPK> {
	
}
