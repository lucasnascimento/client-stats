package br.com.citel.monitoramento;

import java.io.File;

import lombok.Setter;
import lombok.extern.java.Log;

@Log
public class HDMonitor {
	@Setter
	private String monitoraHD;

	public void run() {
		throw new RuntimeException("TODO: Implementação não finalizada");
	}
	public static void getDiskInfo() {
		File[] roots = File.listRoots();
		for (File root : roots) {
			float freeSpacePercent = (float) root.getFreeSpace() / (float) root.getTotalSpace();
			log.info(String.format("filesystem: %s - disponivel: %.2f%%", root.getAbsoluteFile(), freeSpacePercent));
		}
	}
}
