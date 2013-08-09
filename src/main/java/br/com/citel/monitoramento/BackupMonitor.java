package br.com.citel.monitoramento;

import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import lombok.Setter;
import lombok.extern.java.Log;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.citel.monitoramento.entity.LOGBKP;
import br.com.citel.monitoramento.repository.portal.LogbkpRepository;

@Log
public class BackupMonitor {
	@Setter
	private String monitoraBackup;
	@Setter
	private String backupPath;
	@Setter
	private String cnpjEmpresa;
	@Autowired
	private LogbkpRepository logbkpRepository;

	public void run() {
		if ("1".equals(monitoraBackup)) {
			processLogBackup();
			log.info("MONITORAMENTO BACKUP FEITO.");
		}else{
			log.info("MONITORAMENTO BACKUP DESLIGADO.");
		}
	}

	public void processLogBackup() {
		ArrayList<LOGBKP> logBackupList = new ArrayList<LOGBKP>();
		if (StringUtils.isNotBlank(backupPath)) {
			File backupDir = new File(backupPath);
			if (backupDir.exists()) {
				File[] files = backupDir.listFiles();
				for (File file : files) {
					LOGBKP logBackup = new LOGBKP();
					logBackup.setLOG_C_G_C_(cnpjEmpresa);
					logBackup.setLOG_NOMBKP(file.getName());
					logBackup.setLOG_DTABKP(new Date(file.lastModified()));
					logBackup.setLOG_HORBKP(new Time(file.lastModified()));
					logBackup.setLOG_TAMBKP(file.length());
					logBackupList.add(logBackup);
				}
				logbkpRepository.save(logBackupList);
			}
		} else {
			throw new RuntimeException("bacupPath em branco!");
		}
	}
}
