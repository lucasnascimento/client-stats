package br.com.citel.monitoramento;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.citel.monitoramento.entity.CONTMO;
import br.com.citel.monitoramento.repository.autcom.ContloAutComRepository;
import br.com.citel.monitoramento.repository.autcom.ContmoAutComRepository;
import br.com.citel.monitoramento.repository.portal.ContloPortalRepository;
import br.com.citel.monitoramento.repository.portal.ContmoPortalRepository;

/**
 * Classe de responsável por fazer a sincronia do CONTMO e CONLO
 * 
 * @author lucas
 * 
 */
@Log4j
public class DatabaseSync {

	@Setter
	private String enviaDados;
	@Setter
	private Long empresaFisica;
	@Setter
	private String cnpjEmpresa;
	@Autowired
	private ContmoAutComRepository contmoAutComRepository;
	@Autowired
	private ContmoPortalRepository contmoPortalRepository;
	@Autowired
	private ContloAutComRepository contloAutComRepository;
	@Autowired
	private ContloPortalRepository contloPortalRepository;

	public void run() {
		try {
			if ("1".equals(enviaDados)) {
				processaCONTLO();
				log.info("CONTLO ENVIADO PARA O PORTAL");
				processaCONTMO();
				log.info("CONTMO ENVIADO PARA O PORTAL");
			} else {
				log.info("ENVIO DE DADOS DESLIGADO");
			}
		} catch (Throwable t) {
			log.error("ERRO AO PROCESSAR - ENVIO CONTOMO E CONTLO", t);
		}
	}

	private void processaCONTLO() {
		contloPortalRepository.deleteByCNPJ(cnpjEmpresa);
		contloPortalRepository.bulkSaveWithoutCheksExists(contloAutComRepository.findAll());
	}

	private void processaCONTMO() {
		long timeInit = System.currentTimeMillis();
		
		long timeInitDelete = System.currentTimeMillis();
		contmoPortalRepository.deleteByEmpresaFiscaAndCNPJ(empresaFisica, cnpjEmpresa);
		long timeEndDelete = System.currentTimeMillis();
		
		long timeInitGetList = System.currentTimeMillis();
		Iterable<CONTMO> contmoList = contmoAutComRepository.findAll();
		long timeEndGetList = System.currentTimeMillis();
		
		long timeInitBulkSave = System.currentTimeMillis();
		contmoPortalRepository.bulkSaveWithoutCheksExists(contmoList);
		long timeEndBulkSave = System.currentTimeMillis();
		
		long timeEnd = System.currentTimeMillis();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n");
		sb.append(" DELETANDO:   ").append(timeEndDelete - timeInitDelete).append("millis \n");
		sb.append(" RECUPERANDO LISTA:   ").append(timeEndGetList - timeInitGetList).append("millis \n");
		sb.append(" BULKINSER:           ").append(timeEndBulkSave - timeInitBulkSave).append("millis \n");
		sb.append(" TOTAL:               ").append(timeEnd - timeInit).append("millis \n");
		log.info(sb);
		
	}

}
