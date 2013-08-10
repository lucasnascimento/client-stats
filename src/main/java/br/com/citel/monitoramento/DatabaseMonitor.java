package br.com.citel.monitoramento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import lombok.Setter;
import lombok.extern.java.Log;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.citel.monitoramento.entity.LOG_DATA;
import br.com.citel.monitoramento.model.DatabaseTicket;
import br.com.citel.monitoramento.model.Field;
import br.com.citel.monitoramento.model.Index;
import br.com.citel.monitoramento.model.Table;
import br.com.citel.monitoramento.repository.portal.LogdataRepository;

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
public class DatabaseMonitor {

	@Setter
	private String monitoraDatabase;

	@Setter
	private String cnpjEmpresa;

	@Setter
	private DataSource sourceDataSource;

	@Setter
	private DataSource targetDataSource;
	
	@Autowired
	private LogdataRepository logdataRepository;  

	private static List<DatabaseTicket> dbTicketList = new ArrayList<DatabaseTicket>();

	public void run() throws SQLException {
		if ("1".equals(monitoraDatabase)) {
			processDatabaseMonitor();
			log.info("MONITORAMENTO DATABASE FEITO.");
		} else {
			log.info("MONITORAMENTO DATABASE DESLIGADO.");
		}
	}

	public void processDatabaseMonitor() throws SQLException {
		dbTicketList.clear();
		Connection connSource = sourceDataSource.getConnection();
		List<Table> tableSourceList = loadTables(connSource);
		Connection connTarget = targetDataSource.getConnection();
		List<Table> tableTargetList = loadTables(connTarget);
		compareTable(tableSourceList, tableTargetList);

		for (DatabaseTicket dbTicket : dbTicketList) {
			LOG_DATA logData = new LOG_DATA();
			logData.setLOG_C_G_C_(cnpjEmpresa);
			logData.setLOG_TABELA(dbTicket.getTableName());
			logData.setLOG_MENSAGEM(dbTicket.getSubject());
			logdataRepository.save(logData);
		}
	}

	private void compareTable(List<Table> tableSourceList, List<Table> tableTargetList) {
		for (Table tableSource : tableSourceList) {
			int tableTargetIndex = tableTargetList.indexOf((tableSource));
			if (tableTargetIndex < 0) {
				dbTicketList.add(new DatabaseTicket(tableSource.getTableName(), "TABELA NÃO ENCONTRADA NO DATABASE DESTINO"));
			} else {
				Table tableTarget = tableTargetList.remove(tableTargetIndex);
				compareField(tableSource.getTableName(), tableSource.getFields(), tableTarget.getFields());
				compareIndex(tableSource.getTableName(), tableSource.getIndexes(), tableTarget.getIndexes());
			}
		}
		for (Table tableTarget : tableTargetList) {
			dbTicketList.add(new DatabaseTicket(tableTarget.getTableName(), "TABELA NÃO ENCONTRADA NO DATABASE MODELO"));
		}
	}

	private void compareField(String tableName, List<Field> fieldSourceList, List<Field> fieldTargetList) {
		for (Field fieldSource : fieldSourceList) {
			int fieldTargetIndex = fieldTargetList.indexOf(fieldSource);
			if (fieldTargetIndex < 0) {
				dbTicketList.add(new DatabaseTicket(tableName, String.format("CAMPO NÃO ENCONTRADO NO DATABASE DESTINO: %s", fieldSource.getFieldName())));
			} else {
				Field fieldTarget = fieldTargetList.remove(fieldTargetIndex);
				if (!StringUtils.equals(fieldTarget.getType(),fieldSource.getType())) {
					dbTicketList.add(new DatabaseTicket(tableName, String.format("TIPO DE DADOS INCONSISTENTE: NO MODELO: %s %s DESTINO: %s %s ", fieldSource.getFieldName(), fieldSource.getType(),
							fieldTarget.getFieldName(), fieldTarget.getType())));
				}
				if(!StringUtils.equals(fieldTarget.getDef(), fieldSource.getDef())){
					dbTicketList.add(new DatabaseTicket(tableName, String.format("DEFAUL INCONSISTENTE: NO MODELO: %s %s DESTINO: %s %s ", fieldSource.getFieldName(), fieldSource.getDef(),
							fieldTarget.getFieldName(), fieldTarget.getDef())));
				}
				if (fieldTarget.getNullable() != fieldSource.getNullable()) {
					dbTicketList.add(new DatabaseTicket(tableName, String.format("NULL INCONSISTENTE: NO MODELO: %s %s DESTINO: %s %s ", fieldSource.getFieldName(), fieldSource.getNullable(),
							fieldTarget.getFieldName(), fieldTarget.getNullable())));
				}
			}
		}
		for (Field fieldTarget : fieldTargetList) {
			dbTicketList.add(new DatabaseTicket(tableName, String.format("CAMPO NÃO ENCONTRADO NO MODELO: %s", fieldTarget.getFieldName())));
		}
	}

	private void compareIndex(String tableName, List<Index> indexSourceList, List<Index> indexTargetList) {
		for (Index indexSource : indexSourceList) {
			int indexTargetIndex = indexTargetList.indexOf(indexSource);
			if (indexTargetIndex < 0) {
				dbTicketList.add(new DatabaseTicket(tableName, String.format("INDICE NÃO ENCONTRADO NO DESTINO: %s", indexSource.getIndexName())));
			} else {
				Index indexTarget = indexTargetList.remove(indexTargetIndex);
				if (!StringUtils.equals(indexTarget.getColumnName(),indexSource.getColumnName())) {
					dbTicketList.add(new DatabaseTicket(tableName, String.format("INCONSISTÊNCIA DE COLUNAS NO INDÍCE: MODELO: %s %s DESTINO: %s %s ", indexSource.getIndexName(),
							indexSource.getColumnName(), indexTarget.getIndexName(), indexTarget.getColumnName())));
				}
			}
		}
		for (Index indexTarget : indexTargetList) {
			dbTicketList.add(new DatabaseTicket(tableName, String.format("INDÍCE NÃO ENCONTRADO NO MODELO: %s", indexTarget.getIndexName())));
		}
	}

	private List<Table> loadTables(Connection conn) {
		PreparedStatement pstm;
		ResultSet rs;
		List<Table> tables = new ArrayList<Table>();
		try {
			pstm = conn.prepareStatement("show tables;");
			rs = pstm.executeQuery();
			while (rs.next()) {

				String tableName = rs.getString(1);
				List<Field> fields = loadFields(conn, tableName);
				List<Index> indexes = loadIndexes(conn, tableName);
				tables.add(new Table(tableName, fields, indexes));
			}
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(pstm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}

	private List<Field> loadFields(Connection conn, String tableName) {
		PreparedStatement pstm;
		ResultSet rs;
		List<Field> fields = new ArrayList<Field>();
		try {
			pstm = conn.prepareStatement("show columns from " + tableName + ";");
			rs = pstm.executeQuery();

			while (rs.next()) {
				String fieldName = StringUtils.upperCase(rs.getString("field"));
				String type = StringUtils.upperCase(rs.getString("type"));
				Boolean nullable = rs.getString("null").equalsIgnoreCase("YES") ? true : false;
				String key = StringUtils.upperCase(rs.getString("key"));
				String def = StringUtils.upperCase(rs.getString("default"));
				String extra = StringUtils.upperCase(rs.getString("extra"));
				fields.add(new Field(fieldName, type, nullable, key, def, extra));
			}
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(pstm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fields;
	}

	private List<Index> loadIndexes(Connection conn, String tableName) {
		PreparedStatement pstm;
		ResultSet rs;
		List<Index> indexes = new ArrayList<Index>();
		try {
			pstm = conn.prepareStatement("show index from " + tableName + ";");
			rs = pstm.executeQuery();
			while (rs.next()) {
				String indexName = StringUtils.upperCase(rs.getString("Key_name"));
				String columnName = StringUtils.upperCase(rs.getString("Column_name"));
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
