package br.com.citel.monitoramento;

import java.sql.SQLException;

import lombok.extern.log4j.Log4j;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A empresa Citel precisa de um software que monitore e armazene estatísticas
 * de monitoria das instâncias de seu software no ambiene de clientes, os
 * objetivos inciais deste aplicativoão são:
 * 
 * Monitorar Espaço em Disco Monitorar Estrutura de Banco de Dados Monitorar O
 * arquivos gerados por backup Sincronizar CONTMO e CONTLO
 */
@Log4j
public class Main {

	/**
	 * Método Main usado como entrada incial do processo responsável por inciar
	 * o Spring e suas configurações em <code>classpath:spring-beans.xml</code>
	 * 
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		try {
			log.info("INICIO PROCESSAMENTO>>>>>>>>>>>>>>>>>");
			@SuppressWarnings({ "resource" })
			ApplicationContext appContext = new ClassPathXmlApplicationContext("spring-beans.xml");
			
			LogNotMonitor lognotMonitor = appContext.getBean(LogNotMonitor.class);
			lognotMonitor.finalizaMonitoramento();
			
			log.info("FIM PROCESSAMENTO>>>>>>>>>>>>>>>>>");
		} catch (Throwable t) {
			log.error("ERRO AO PROCESSAR - GERAL", t);
		}
	}
}
