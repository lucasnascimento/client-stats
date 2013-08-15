package br.com.citel.monitoramento.util;

import javax.sql.DataSource;

import lombok.Setter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class DbPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {
	@Setter
	protected String empresaFisica;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		DataSource dataSource = beanFactory.getBean("autcomDS", DataSource.class);

		DbProperties dbProps = new DbProperties(dataSource, empresaFisica);

		setProperties(dbProps);
		
		super.postProcessBeanFactory(beanFactory);

	}

}