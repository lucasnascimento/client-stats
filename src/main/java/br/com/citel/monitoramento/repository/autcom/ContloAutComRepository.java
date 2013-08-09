package br.com.citel.monitoramento.repository.autcom;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.CONTLO;
import br.com.citel.monitoramento.entity.CONTLOPK;

@Repository
public interface ContloAutComRepository extends CrudRepository<CONTLO, CONTLOPK> {
}
