package br.com.citel.monitoramento;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import lombok.extern.java.Log;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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

	@Autowired
	private LogdataRepository logdataRepository;

	@Autowired
	private JdbcTemplate sourceJdbcTemplate;

	@Autowired
	private JdbcTemplate targetJdbcTemplate;

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
		List<Table> tableSourceList = loadTables(sourceJdbcTemplate);
		List<Table> tableTargetList = loadTables(targetJdbcTemplate);
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
				if (!StringUtils.equals(fieldTarget.getType(), fieldSource.getType())) {
					dbTicketList.add(new DatabaseTicket(tableName, String.format("TIPO DE DADOS INCONSISTENTE: NO MODELO: %s %s DESTINO: %s %s ", fieldSource.getFieldName(), fieldSource.getType(),
							fieldTarget.getFieldName(), fieldTarget.getType())));
				}
				if (!StringUtils.equals(fieldTarget.getDef(), fieldSource.getDef())) {
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
				if (!StringUtils.equals(indexTarget.getColumnName(), indexSource.getColumnName())) {
					dbTicketList.add(new DatabaseTicket(tableName, String.format("INCONSISTÊNCIA DE COLUNAS NO INDÍCE: MODELO: %s %s DESTINO: %s %s ", indexSource.getIndexName(),
							indexSource.getColumnName(), indexTarget.getIndexName(), indexTarget.getColumnName())));
				}
			}
		}
		for (Index indexTarget : indexTargetList) {
			dbTicketList.add(new DatabaseTicket(tableName, String.format("INDÍCE NÃO ENCONTRADO NO MODELO: %s", indexTarget.getIndexName())));
		}
	}

	private List<Table> loadTables(final JdbcTemplate jdbcTemplate) {
		return jdbcTemplate.query("show tables", new RowMapper<Table>() {
			@Override
			public Table mapRow(ResultSet rs, int rownumber) throws SQLException {
				String tableName = rs.getString(1);
				return new Table(tableName, loadFields(jdbcTemplate, tableName), loadIndexes(jdbcTemplate, tableName));
			}
		});
	}

	private List<Field> loadFields(final JdbcTemplate jdbcTemplate, final String tableName) {
		return jdbcTemplate.query("show columns from " + tableName, new RowMapper<Field>() {
			@Override
			public Field mapRow(ResultSet rs, int rownumber) throws SQLException {
				String fieldName = StringUtils.upperCase(rs.getString("field"));
				String type = StringUtils.upperCase(rs.getString("type"));
				Boolean nullable = rs.getString("null").equalsIgnoreCase("YES") ? true : false;
				String key = StringUtils.upperCase(rs.getString("key"));
				String def = StringUtils.upperCase(rs.getString("default"));
				String extra = StringUtils.upperCase(rs.getString("extra"));
				return new Field(fieldName, type, nullable, key, def, extra);
			}
		});
	}

	private List<Index> loadIndexes(final JdbcTemplate jdbcTemplate, final String tableName) {
		return jdbcTemplate.query("show index from  " + tableName, new RowMapper<Index>() {
			@Override
			public Index mapRow(ResultSet rs, int rownumber) throws SQLException {
				String indexName = StringUtils.upperCase(rs.getString("Key_name"));
				String columnName = StringUtils.upperCase(rs.getString("Column_name"));
				return new Index(indexName, columnName);
			}
		});
	}

}
