package br.com.citel.client_stats;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.java.Log;

import org.apache.commons.dbutils.DbUtils;

import br.com.citel.client_stats.model.Field;
import br.com.citel.client_stats.model.Index;
import br.com.citel.client_stats.model.Table;
import br.com.citel.client_stats.util.DBConnection;

/**
 * A empresa Citel precisa de um software que monitore e armazene estatísticas
 * de monitoria das instâncias de seu software no ambiene de clientes, os
 * objetivos inciais deste aplicativoão são:
 * 
 * Monitorar Espaço em Disco Motorar Estrutura de Banco de Dados
 * 
 * 
 */
@Log
public class Main {
	public static void main(String[] args) throws SQLException {
		processDatabaseValidation();
		// getDiskInfo();

	}

	private static void processDatabaseValidation() throws SQLException {
		Connection connSource = DBConnection.getConnectionSource();
		List<Table> tableSource = loadTables(connSource);
		Connection connTarget = DBConnection.getConnectionTarget();
		List<Table> tableTarget = loadTables(connTarget);
		DbUtils.close(connSource);
		DbUtils.close(connTarget);
		System.out.println(tableTarget);
		compareTable(tableSource, tableTarget);
		System.out.println(tableTarget);
	}

	private static void compareTable(List<Table> tableSourceList,
			List<Table> tableTargetList) {

		for (Table tableSource : tableSourceList) {
			int tableTargetIndex = tableTargetList.indexOf((tableSource));
			if (tableTargetIndex < 0) {
				log.info(String.format("table target not found: %s",
						tableSource.getTableName()));
			} else {
				Table tableTarget = tableTargetList.remove(tableTargetIndex);
				compareField(tableSource.getFields(), tableTarget.getFields());
				compareIndex(tableSource.getIndexes(), tableTarget.getIndexes());
			}

		}

	}

	private static void compareField(List<Field> fieldSourceList,
			List<Field> fieldTargetList) {
		for (Field fieldSource : fieldSourceList) {
			int fieldTargetIndex = fieldTargetList.indexOf(fieldSource);
			if (fieldTargetIndex < 0) {
				log.info(String.format("field target not found: %s",
						fieldSource.getFieldName()));
			} else {
				Field fieldTarget = fieldTargetList.remove(fieldTargetIndex);
				if (!fieldTarget.getType().equalsIgnoreCase(
						fieldSource.getType())) {
					log.info(String
							.format("field target's type inconsistency: %s - Source: %s %s Target: %s %s ",
									fieldSource.getFieldName(),
									fieldSource.getType(),
									fieldTarget.getFieldName(),
									fieldTarget.getType()));
				}
			}
		}
		for (Field fieldTarget : fieldTargetList) {
			log.info(String.format("field not found on source table: %s",
					fieldTarget.getFieldName()));
		}
	}

	private static void compareIndex(List<Index> indexSourceList,
			List<Index> indexTargetList) {
		for (Index indexSource : indexSourceList) {
			int indexTargetIndex = indexTargetList.indexOf(indexSource);
			if (indexTargetIndex < 0) {
				log.info(String.format("index target not found: %s",
						indexSource.getIndexName()));
			} else {
				Index indexTarget = indexTargetList.remove(indexTargetIndex);
				if (!indexTarget.getColumnName().equalsIgnoreCase(
						indexSource.getColumnName())) {
					log.info(String
							.format("index target's columns inconsistency: %s - Source: %s %s Target: %s %s ",
									indexSource.getIndexName(),
									indexSource.getColumnName(),
									indexTarget.getIndexName(),
									indexTarget.getColumnName()));
				}
			}
		}
		for (Index indexTarget : indexTargetList) {
			log.info(String.format("index not found on source table: %s",
					indexTarget.getIndexName()));
		}
	}

	public static void getDiskInfo() {
		File[] roots = File.listRoots();
		for (File root : roots) {
			float freeSpacePercent = (float) root.getFreeSpace()
					/ (float) root.getTotalSpace();
			log.info(String.format("filesystem: %s - disponivel: %.2f%%",
					root.getAbsoluteFile(), freeSpacePercent));
		}
	}

	private static List<Table> loadTables(Connection conn) {
		PreparedStatement pstm;
		ResultSet rs;
		List<Table> tables = new ArrayList<Table>();
		try {
			pstm = conn.prepareStatement("show tables;");
			rs = pstm.executeQuery();
			while (rs.next()) {

				String tableName = rs.getString(1);
				log.info("TABELA - Carregando: ".concat(tableName));
				List<Field> fields = loadFields(conn, tableName);
				List<Index> indexes = loadIndexes(conn, tableName);
				tables.add(new Table(tableName, fields, indexes));
				log.info("TABELA - Carregada: ".concat(tableName));
			}
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(pstm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}

	private static List<Field> loadFields(Connection conn, String tableName) {
		PreparedStatement pstm;
		ResultSet rs;
		List<Field> fields = new ArrayList<Field>();
		try {
			pstm = conn
					.prepareStatement("show columns from " + tableName + ";");
			rs = pstm.executeQuery();

			while (rs.next()) {
				String fieldName = rs.getString("field");
				String type = rs.getString("type");
				Boolean nullable = rs.getString("null").equalsIgnoreCase("YES") ? true
						: false;
				String key = rs.getString("key");
				String def = rs.getString("default");
				String extra = rs.getString("extra");

				fields.add(new Field(fieldName, type, nullable, key, def, extra));

			}
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(pstm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fields;
	}

	private static List<Index> loadIndexes(Connection conn, String tableName) {
		PreparedStatement pstm;
		ResultSet rs;
		List<Index> indexes = new ArrayList<Index>();
		try {
			pstm = conn.prepareStatement("show index from " + tableName + ";");
			rs = pstm.executeQuery();

			while (rs.next()) {
				String indexName = rs.getString("Key_name");
				String columnName = rs.getString("Column_name");

				indexes.add(new Index(indexName, columnName));

			}
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(pstm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return indexes;
	}
}
