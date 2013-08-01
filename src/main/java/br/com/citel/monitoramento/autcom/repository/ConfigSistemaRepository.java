package br.com.citel.monitoramento.autcom.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.CFGSIS;

@Repository
public interface ConfigSistemaRepository extends CrudRepository<CFGSIS, Long> {

	@Query("select c from ConfigSistema c where c.codigoEmpresa = ?1 and c.codigoConfiguracao = ?2")
	CFGSIS getConfigSistema(String codigoEmpresa, String codigoConfiguracao);
	
}
