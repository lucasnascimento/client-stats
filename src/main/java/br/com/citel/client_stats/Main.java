package br.com.citel.client_stats;

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
		Connection conn = DBConnection.getConnection();
		List<Table> tableList = loadTables(conn);	
		DbUtils.close(conn);
		System.out.println(tableList);
	}
	
	private static List<Table> loadTables(Connection conn){
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
				tables.add( new Table (tableName, fields, indexes));
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
			pstm = conn.prepareStatement("show columns from "+ tableName +";");
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				String fieldName = rs.getString("field");
				String type = rs.getString("type");
				Boolean nullable = rs.getString("null").equalsIgnoreCase("YES") ? true : false  ;
				String key = rs.getString("key");
				String def = rs.getString("default");
				String extra = rs.getString("extra");
				
				fields.add( new Field (fieldName,type,nullable,key,def,extra) );
				
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
			pstm = conn.prepareStatement("show index from "+ tableName +";");
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				String indexName = rs.getString("Key_name");
				String columnName = rs.getString("Column_name");

				
				indexes.add( new Index (indexName,columnName) );
				
			}
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(pstm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return indexes;
	}
}
