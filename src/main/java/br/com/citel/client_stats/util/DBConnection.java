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

	public static Connection getConnectionSource() {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", "root");
		connectionProps.put("password", "123456");

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/SourceDatabase", connectionProps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static Connection getConnectionTarget() {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", "root");
		connectionProps.put("password", "123456");

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/TargetDatabase", connectionProps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static Connection getConnectionTest() {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", "root");
		connectionProps.put("password", "123456");

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", connectionProps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

}
