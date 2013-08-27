package br.com.citel.monitoramento.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Classe utilitário para recuperar os dados configuraveis do banco de dados.
 * 
 * @author lucas
 * 
 */
@Log4j
public class DbProperties extends Properties {
	private static final long serialVersionUID = 1L;

	public DbProperties(DataSource dataSource, String empresaFisica) {
		super();

		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		Map<String, String> parameters = new HashMap<String, String>();
		StringBuilder sql = new StringBuilder();

		sql.append("select SIS_CODCFG AS CHAVE, SIS_VALCFG AS VALOR from CFGSIS where SIS_CODEMP = :empFisica and SIS_GRUCFG = 'monitoramento'");
		parameters.put("empFisica", empresaFisica);
		List<Map<String, Object>> l = jdbcTemplate.queryForList(sql.toString(), parameters);

		for (Map<String, Object> m : l) {

			String chave = (m.get("CHAVE")).toString();
			String valor = (m.get("VALOR")).toString();

			if ("AUTCOM_VERSAO".equalsIgnoreCase(chave)) {
				valor = valor.replace(".", "_");
			}

			log.debug(String.format("Loading from DB: [{%s}:{%s}]", chave, valor));
			setProperty(chave, valor);
		}
		
		sql = new StringBuilder();
		sql
		.append(" select 'AUTCOM_VERSAO' AS CHAVE, INF_VERARQ AS VALOR from INFSIS where INF_NOMARQ = 'autcom.exe'").append(" union")
		.append(" select 'EMP_C_G_C_', EMP_C_G_C_ from CADEMP WHERE EMP_CODEMP = :empFisica");
		parameters.put("empFisica", empresaFisica);
		l = jdbcTemplate.queryForList(sql.toString(), parameters);

		for (Map<String, Object> m : l) {

			String chave = (m.get("CHAVE")).toString();
			String valor = (m.get("VALOR")).toString();

			if ("AUTCOM_VERSAO".equalsIgnoreCase(chave)) {
				valor = valor.replace(".", "_");
			}

			log.debug(String.format("Loading from DB: [{%s}:{%s}]", chave, valor));
			setProperty(chave, valor);
		}


		if (!containsKey("AUTCOM_VERSAO")) {
			setProperty("AUTCOM_VERSAO", "AUTCOM");
		}

		setProperty("empFisic", empresaFisica);

		log.info("PROPRIEDADES CARREGADAS: " + this.toString());

	}
}