package br.com.citel.monitoramento.autcom.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.CONTMO;
import br.com.citel.monitoramento.entity.CONTMOPK;

@Repository
public interface ContmoAutComRepository extends CrudRepository<CONTMO, CONTMOPK> {
}
