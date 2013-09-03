package br.com.citel.monitoramento;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;

import br.com.citel.monitoramento.entity.CONTMO;
import br.com.citel.monitoramento.repository.autcom.ContloAutComRepository;
import br.com.citel.monitoramento.repository.autcom.ContmoAutComRepository;
import br.com.citel.monitoramento.repository.portal.ContloPortalRepository;
import br.com.citel.monitoramento.repository.portal.ContmoPortalRepository;

import com.mysql.jdbc.Statement;


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
	@Autowired
	private DataSource portalDS;

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
		contmoPortalRepository.deleteByEmpresaFiscaAndCNPJ(empresaFisica, cnpjEmpresa);
		Iterable<CONTMO> contmoList = contmoAutComRepository.findAll();
		Connection mysqlConn ;
		Statement mySQLstm;
		try {
			
			C3P0NativeJdbcExtractor extractor = new C3P0NativeJdbcExtractor();
			mysqlConn = (com.mysql.jdbc.Connection) extractor.getNativeConnection(C3P0NativeJdbcExtractor.getRawConnection(portalDS.getConnection()));
			mySQLstm = (Statement) mysqlConn.createStatement();
			
			String sql = "LOAD DATA LOCAL INFILE  'contmo.txt'  INTO TABLE CONTMO FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			StringBuilder builder = new StringBuilder();
			for(CONTMO contmo : contmoList){
				builder.append(contmo.getQTD_REG())
				.append(',')
				.append(contmo.getTABELA())
				.append(',')
				.append(sdf.format(contmo.getDIA()))
				.append(',')
				.append( String.format("%03d",contmo.getEMPRESA()))
				.append(',')
				.append(String.format("%03d",contmo.getEMPRESA_FISICA()))
				.append(',')
				.append(contmo.getCNPJ())
				.append('\n');
			}
			
			InputStream is = IOUtils.toInputStream(builder.toString());
			mySQLstm.setLocalInfileInputStream(is);
			
			mySQLstm.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
