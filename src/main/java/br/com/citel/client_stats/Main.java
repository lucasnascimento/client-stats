package br.com.citel.client_stats;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.citel.client_stats.entity.ConfigSistema;
import br.com.citel.client_stats.repository.ConfigSistemaRepository;

public class Main {
	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("client-stats-beans.xml");
		
		ConfigSistemaRepository configSistemaRepository = appContext.getBean(ConfigSistemaRepository.class);
		ConfigSistema cfgsis = configSistemaRepository.getConfigSistema("001", "CFG_MONRLR");
		
		System.out.println(cfgsis);
	}
}
