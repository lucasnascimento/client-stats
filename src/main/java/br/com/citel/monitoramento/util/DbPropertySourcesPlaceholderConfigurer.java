package br.com.citel.monitoramento.util;

import java.lang.reflect.Field;
import java.util.Properties;

import javax.sql.DataSource;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;

/**
 * Classe utilitário para recuperar os dados configuraveis do banco de dados.
 * Utlizando o SpringFramework
 * 
 * @author lucas
 * 
 */
@Log4j
public class DbPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		log.info("CARREGANDO PROPRIEDADES DO BANCO DE DADOS LOCAL");
		DataSource dataSource = beanFactory.getBean("autcomDS", DataSource.class);

		PropertySourcesPlaceholderConfigurer propertiesFromMonitorINI = beanFactory.getBean("propertiesFromMonitorINI", org.springframework.context.support.PropertySourcesPlaceholderConfigurer.class);

		try {
			Field privateField = PropertySourcesPlaceholderConfigurer.class.getDeclaredField("propertySources");
			privateField.setAccessible(true);
			MutablePropertySources mps = (MutablePropertySources) privateField.get(propertiesFromMonitorINI);

			Properties propsFinalmente = (Properties) mps.get("localProperties").getSource();
			String empresaFisica = (String) propsFinalmente.getProperty("empFisic");

			DbProperties dbProps = new DbProperties(dataSource, empresaFisica);

			setProperties(dbProps);

			super.postProcessBeanFactory(beanFactory);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}