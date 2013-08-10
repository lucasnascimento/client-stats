package br.com.citel.monitoramento.repository.portal;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.LOG_DATA;
import br.com.citel.monitoramento.entity.LOG_DATAPK;

@Repository
public interface LogdataRepository extends CrudRepository<LOG_DATA, LOG_DATAPK> {
}
