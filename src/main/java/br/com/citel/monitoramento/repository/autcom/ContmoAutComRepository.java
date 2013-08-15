package br.com.citel.monitoramento.repository.autcom;

import org.springframework.data.repository.CrudRepository;
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
public interface ContmoAutComRepository extends CrudRepository<CONTMO, CONTMOPK> {
}
