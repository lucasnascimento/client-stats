package br.com.citel.client_stats.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
	
	static DBProperties prop = DBProperties.getInstance();
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public static Connection getConnectionSource() {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", prop.getSourceUser());
		connectionProps.put("password", prop.getSourcePassword());
		try {
			String connectionString = String.format("jdbc:mysql://%s:%s/%s", prop.getSourceHost(), prop.getSourcePort(), prop.getSourceDatebase());
			conn = DriverManager.getConnection(connectionString, connectionProps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static Connection getConnectionTarget() {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", prop.getTargetUser());
		connectionProps.put("password", prop.getTargetPassword());
		try {
			String connectionString = String.format("jdbc:mysql://%s:%s/%s", prop.getTargetHost(), prop.getTargetPort(), prop.getTargetDatebase());
			conn = DriverManager.getConnection(connectionString, connectionProps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}


}
