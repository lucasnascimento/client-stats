package br.com.citel.monitoramento;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mchange.v2.c3p0.PooledDataSource;

public class Main {


	public static void main(String[] args) throws SQLException {
		@SuppressWarnings({ "resource" })
		ApplicationContext appContext = new ClassPathXmlApplicationContext("spring-beans.xml");
		
		PooledDataSource portalPooled = (PooledDataSource)  appContext.getBean("portalDS");
		
		portalPooled.close();

	}
}
