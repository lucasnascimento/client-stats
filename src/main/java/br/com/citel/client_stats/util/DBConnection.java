package br.com.citel.client_stats.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public static Connection getConnectionSource() throws SQLException {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", "root");
		connectionProps.put("password", "root");

		conn = DriverManager.getConnection("jdbc:mysql://192.168.0.105/AUTCOM",
				connectionProps);
		return conn;
	}
	
	public static Connection getConnectionTarget() throws SQLException {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", "root");
		connectionProps.put("password", "123456");

		conn = DriverManager.getConnection("jdbc:mysql://localhost/AUTCOM",
				connectionProps);
		return conn;
	}

}
