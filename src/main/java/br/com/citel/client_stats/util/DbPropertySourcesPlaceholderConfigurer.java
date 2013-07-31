package br.com.citel.client_stats.util;

import javax.sql.DataSource;

import lombok.Setter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
 
public class DbPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer
{
	@Setter
	protected String empresaFisica;

		
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		
		
		DataSource dataSource = beanFactory.getBean("dataSource", DataSource.class);

        DbProperties dbProps = new DbProperties(dataSource,empresaFisica);
         
        setProperties(dbProps);
        super.postProcessBeanFactory(beanFactory);
		
		
	}
	
}