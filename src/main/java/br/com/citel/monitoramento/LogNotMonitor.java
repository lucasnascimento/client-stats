package br.com.citel.monitoramento;

import java.util.Date;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Classe de monitoramento responsável por fazer o monitoramento do HD
 * 
 * @author lucas
 * 
 */
@Log4j
public class LogNotMonitor {

	@Setter
	private String cnpjEmpresa;
	@Autowired
	private JdbcTemplate portalJdbcTemplate;

	private Date dataProcessamento = new Date();
	
	private String versaoMonitoramento = "monitoramento-0.0.4";

	public void inicializaMonitoramento() {
		portalJdbcTemplate.update("INSERT INTO LOGNOT SET LOG_C_G_C_ = ?, LOG_HORINI = ?, LOG_DESCRI = ?, LOG_DTABAS = NOW()"
				, new Object[] { cnpjEmpresa, dataProcessamento, versaoMonitoramento });
		log.info("LOGNOT - INSERIDO INICIANDO PROCESSO");
	}

	public void finalizaMonitoramento() {
		portalJdbcTemplate.update("UPDATE LOGNOT SET LOG_HORFIN = ?, LOG_EXECUT = 'S' WHERE LOG_DESCRI = ? AND LOG_HORINI = ? AND LOG_C_G_C_ = ?"
				, new Object[] { new Date(), versaoMonitoramento, dataProcessamento, cnpjEmpresa  });
		log.info("LOGNOT - FINALIZADO PROCESSAMENTO");
	}
		
}
