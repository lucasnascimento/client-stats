package br.com.citel.monitoramento.repository.portal;

import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.LOG_HD;
import br.com.citel.monitoramento.entity.LOG_HDPK;
import br.com.citel.monitoramento.repository.CustomRepository;

/**
 * Classe de repositório usando o SpringFramework para facilidades de CRUD.
 * 
 * @author lucas
 * 
 */
@Repository
public interface LoghdRepository extends CustomRepository<LOG_HD, LOG_HDPK> {
}
