package br.com.citel.client_stats.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.citel.client_stats.entity.ConfigSistema;

@Repository
public interface ConfigSistemaRepository extends CrudRepository<ConfigSistema, Long> {

	@Query("select c from ConfigSistema c where c.codigoEmpresa = ?1 and c.codigoConfiguracao = ?2")
	ConfigSistema getConfigSistema(String codigoEmpresa, String codigoConfiguracao);
	
}
