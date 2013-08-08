package br.com.citel.monitoramento;


public class MonitorTest {

//	final String NO_PROBLEM = "noproblem.sql";
//	final String NEW_TABLE2_ON_SOURCE = "newtable2onsource.sql";
//	final String LEFT_TABLE2_ON_TARGE = "lefttable2ontarget.sql";
//	final String FIELD_PROBLEM = "fieldProblem.sql";
//	final String INDEX_PROBLEM = "indexProblem.sql";
//
//	@Test
//	public void testConnectionTest() throws SQLException {
//		Assert.assertNotNull(DBConnection.getConnectionSource());
//	}
//
//	@Test
//	public void testNoProblem() throws SQLException {
//		Connection testConn = DBConnection.getConnectionSource();
//		ScriptRunner scriptRunner = new ScriptRunner(testConn, true, true);
//		scriptRunner.runScriptFromClasspath(NO_PROBLEM);
//		DbUtils.closeQuietly(testConn);
//		List<DatabaseTicket> dbTckets = DatabaseMonitor.processDatabaseValidation();
//		Assert.assertTrue(dbTckets.isEmpty());
//	}
//
//	@Test
//	public void testNewTable2OnSource() throws SQLException {
//		Connection testConn = DBConnection.getConnectionSource();
//		ScriptRunner scriptRunner = new ScriptRunner(testConn, true, true);
//		scriptRunner.runScriptFromClasspath(NEW_TABLE2_ON_SOURCE);
//		DbUtils.closeQuietly(testConn);
//		List<DatabaseTicket> dbTckets = DatabaseMonitor.processDatabaseValidation();
//		Assert.assertTrue(dbTckets.size() == 1);
//	}
//
//	@Test
//	public void testLeftTable2OnTarget() throws SQLException {
//		Connection testConn = DBConnection.getConnectionSource();
//		ScriptRunner scriptRunner = new ScriptRunner(testConn, true, true);
//		scriptRunner.runScriptFromClasspath(LEFT_TABLE2_ON_TARGE);
//		DbUtils.closeQuietly(testConn);
//		List<DatabaseTicket> dbTckets = DatabaseMonitor.processDatabaseValidation();
//		Assert.assertTrue(dbTckets.size() == 1);
//	}
//
//	@Test
//	public void testFieldProblem() throws SQLException {
//		Connection testConn = DBConnection.getConnectionSource();
//		ScriptRunner scriptRunner = new ScriptRunner(testConn, true, true);
//		scriptRunner.runScriptFromClasspath(FIELD_PROBLEM);
//		DbUtils.closeQuietly(testConn);
//		List<DatabaseTicket> dbTckets = DatabaseMonitor.processDatabaseValidation();
//		Assert.assertTrue(dbTckets.size() == 3);
//	}
//
//	@Test
//	public void testIndexProblem() throws SQLException {
//		Connection testConn = DBConnection.getConnectionSource();
//		ScriptRunner scriptRunner = new ScriptRunner(testConn, true, true);
//		scriptRunner.runScriptFromClasspath(INDEX_PROBLEM);
//		DbUtils.closeQuietly(testConn);
//		List<DatabaseTicket> dbTckets = DatabaseMonitor.processDatabaseValidation();
//		Assert.assertTrue(dbTckets.size() == 3);
//	}

}
