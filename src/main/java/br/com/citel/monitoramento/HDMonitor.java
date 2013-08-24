package br.com.citel.monitoramento;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.citel.monitoramento.entity.LOG_HD;
import br.com.citel.monitoramento.repository.portal.LoghdRepository;

/**
 * Classe de monitoramento responsável por fazer o monitoramento do HD
 * 
 * @author lucas
 * 
 */
@Log4j
public class HDMonitor {
	@Setter
	private String monitoraHD;
	@Setter
	private String cnpjEmpresa;
	@Autowired
	private LoghdRepository loghdRepository;

	public void run() {
		try {
			if ("1".equals(monitoraHD)) {
				processHDMonitor();
				log.info("MONITORAMENTO HD FEITO.");
			} else {
				log.info("MONITORAMENTO HD DESLIGADO.");
			}
		} catch (Throwable t) {
			log.error("ERRO AO PROCESSAR", t);
		}
	}

	public void processHDMonitor() {

		try {
			ArrayList<LOG_HD> loghdList = new ArrayList<LOG_HD>();
			Process p = Runtime.getRuntime().exec("df -h");
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				List<String> columns = new ArrayList<String>();
				for (String column : Arrays.asList(line.split("  "))) {
					if (StringUtils.isNotBlank(column)) {
						columns.add(column.trim());
					}
				}
				if (columns.size() == 6) {
					LOG_HD loghd = new LOG_HD();
					loghd.setLOG_C_G_C_(cnpjEmpresa);
					loghd.setLOG_DISPON(columns.get(3));
					loghd.setLOG_MONTAG(columns.get(5));
					loghd.setLOG_PERUSO(Long.valueOf(columns.get(4).replace("%", "")));
					loghd.setLOG_SISARQ(columns.get(0));
					loghd.setLOG_TAMANH(columns.get(1));
					loghd.setLOG_USADO_(columns.get(2));
					loghd.setLOG_VERSAO(" ");
					loghdList.add(loghd);
				}
			}
			loghdRepository.save(loghdList);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
