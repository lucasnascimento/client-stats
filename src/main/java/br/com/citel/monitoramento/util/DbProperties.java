package br.com.citel.monitoramento.util;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import lombok.extern.java.Log;

import org.springframework.jdbc.core.JdbcTemplate;

@Log
public class DbProperties extends Properties {
	private static final long serialVersionUID = 1L;

	public DbProperties(DataSource dataSource, String empresaFisica) {
		super();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<Map<String, Object>> l = jdbcTemplate.queryForList("select sis_codcfg, sis_valcfg from CFGSIS where sis_codemp = ? and sis_grucfg = 'monitoramento'", new Object[] { empresaFisica });

		for (Map<String, Object> m : l) {
			log.info(String.format("Loading from DB: [{%s}:{%s}]", m.get("sis_codcfg"), m.get("sis_valcfg")));
			setProperty((m.get("sis_codcfg")).toString(), (m.get("sis_valcfg")).toString());
		}

		l = jdbcTemplate.queryForList("select 'AUTCOM_VERSAO', INF_VERARQ from INFSIS where INF_NOMARQ = 'autcom.exe'");

		if (l.isEmpty()) {
			setProperty("AUTCOM_VERSAO", "AUTCOM");
		} else {
			for (Map<String, Object> m : l) {
				log.info(String.format("Loading from DB: [{%s}:{%s}]", m.get("AUTCOM_VERSAO"), m.get("INF_VERARQ")));
				String infVersaoArquivo = "AUTCOM_" + (m.get("INF_VERARQ")).toString().replace('.', '_');
				setProperty((m.get("AUTCOM_VERSAO")).toString(), (infVersaoArquivo));
			}
		}

		l = jdbcTemplate.queryForList("select EMP_C_G_C_ from CADEMP WHERE EMP_CODEMP = ?", new Object[] { empresaFisica });

		for (Map<String, Object> m : l) {
			log.info(String.format("Loading from DB: [{EMP_C_G_C_}:{%s}]", m.get("EMP_C_G_C_")));
			setProperty("EMP_C_G_C_", (m.get("EMP_C_G_C_")).toString());
		}

	}
}