package br.com.citel.monitoramento;

import lombok.Setter;

public class BackupMonitor {
	@Setter
	private String monitoraBackup;
	@Setter
	private String backupPath;

	public void run() {
		throw new RuntimeException("TODO: Implementação não finalizada");
	}
}
