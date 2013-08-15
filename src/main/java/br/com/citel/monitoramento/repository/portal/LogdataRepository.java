package br.com.citel.monitoramento.repository.portal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.citel.monitoramento.entity.LOG_DTA;
import br.com.citel.monitoramento.entity.LOG_DTAPK;

/**
 * Classe de repositório usando o SpringFramework para facilidades de CRUD.
 * 
 * @author lucas
 * 
 */
@Repository
public interface LogdataRepository extends JpaRepository<LOG_DTA, LOG_DTAPK> {
	@Query("select l from LOG_DTA l where l.LOG_C_G_C_ = ?1")
	List<LOG_DTA> findByCNPJ(String cnpj);
}
