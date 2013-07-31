package br.com.citel.monitoramento;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.citel.monitoramento.entity.ConfigSistema;
import br.com.citel.monitoramento.repository.ConfigSistemaRepository;

public class Main {
	
	public static void main(String[] args) throws SQLException {
		@SuppressWarnings("resource")
		ApplicationContext appContext = new ClassPathXmlApplicationContext("client-stats-beans.xml");
		
		ConfigSistemaRepository configSistemaRepository = appContext.getBean(ConfigSistemaRepository.class);
		ConfigSistema cfgsis = configSistemaRepository.getConfigSistema("001", "CFG_MONRLR");
		
		
		DataSource ds2 = appContext.getBean("portalDS", DataSource.class);
		ds2.getConnection();
		
		System.out.println(cfgsis);
	}
}
