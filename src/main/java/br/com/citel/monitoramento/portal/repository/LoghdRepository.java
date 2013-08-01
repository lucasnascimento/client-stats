package br.com.citel.monitoramento.portal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.LOG_HD;

@Repository
public interface LoghdRepository extends CrudRepository<LOG_HD, Long> {
	
}
