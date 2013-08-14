package br.com.citel.monitoramento;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) throws SQLException {
		@SuppressWarnings({ "resource", "unused" })
		ApplicationContext appContext = new ClassPathXmlApplicationContext("spring-beans.xml");
	}
}
