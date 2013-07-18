package br.com.citel.client_stats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.junit.Assert;
import org.junit.Test;

import br.com.citel.client_stats.model.DatabaseTicket;
import br.com.citel.client_stats.util.DBConnection;

public class MonitorTest {

	final String CREATE_DATABASES = "createDatabases.sql";

	@Test
	public void testConnectionTest() throws SQLException {
		Assert.assertNotNull(DBConnection.getConnectionTest());
	}

	@Test
	public void testApp() throws SQLException {
		Connection testConn = DBConnection.getConnectionTest();
		executeScriptFile(testConn, CREATE_DATABASES);
		DbUtils.closeQuietly(testConn);
		List<DatabaseTicket> dbTckets = Monitor.processDatabaseValidation();
		Assert.assertTrue( dbTckets.isEmpty() );
	}

	private void executeScriptFile(Connection conn, String scriptFileName) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(new File(this.getClass().getClassLoader().getResource(scriptFileName).toURI())));
			StringBuilder sql = new StringBuilder();
			String linha;
			while ((linha = reader.readLine()) != null) {
				sql.append(linha).append("\n");
			}
			Statement stm = conn.prepareStatement(sql.toString());
			stm.executeBatch();
			DbUtils.closeQuietly(stm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
