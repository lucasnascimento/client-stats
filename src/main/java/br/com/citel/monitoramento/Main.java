package br.com.citel.monitoramento;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.citel.monitoramento.autcom.entity.ConfigSistema;
import br.com.citel.monitoramento.autcom.repository.ConfigSistemaRepository;
import br.com.citel.monitoramento.portal.repository.LogbkpRepository;
import br.com.citel.monitoramento.portal.repository.LoghdRepository;

public class Main {
	
	public static void main(String[] args) throws SQLException {
		@SuppressWarnings("resource")
		ApplicationContext appContext = new ClassPathXmlApplicationContext("client-stats-beans.xml");
		
		ConfigSistemaRepository configSistemaRepository = appContext.getBean(ConfigSistemaRepository.class);
		ConfigSistema cfgsis = configSistemaRepository.getConfigSistema("001", "CFG_MONRLR");
		System.out.println(cfgsis);

		LogbkpRepository logbkpRepository = appContext.getBean(LogbkpRepository.class);
		LoghdRepository loghdRepository = appContext.getBean(LoghdRepository.class);
		
		System.out.println(logbkpRepository.count());
		System.out.println(loghdRepository.count());
		
	}
}
