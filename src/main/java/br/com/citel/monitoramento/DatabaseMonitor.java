package br.com.citel.monitoramento;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import br.com.citel.monitoramento.entity.LOG_DTA;
import br.com.citel.monitoramento.model.DatabaseTicket;
import br.com.citel.monitoramento.model.Field;
import br.com.citel.monitoramento.model.ForeignKey;
import br.com.citel.monitoramento.model.Index;
import br.com.citel.monitoramento.model.Table;
import br.com.citel.monitoramento.repository.portal.LogdataRepository;

/**
 * Classe de monitoramento responsável por fazer o monitoramento da extrutura do
 * Database
 * 
 * @author lucas
 * 
 */
@Log4j
public class DatabaseMonitor {

	@Setter
	private String monitoraDatabase;

	@Setter
	private String cnpjEmpresa;
	
	@Setter
	private String sourceName;
	
	@Setter 
	private String targetName;

	@Autowired
	private LogdataRepository logdataRepository;

	@Autowired
	private JdbcTemplate sourceJdbcTemplate;

	@Autowired
	private JdbcTemplate targetJdbcTemplate;

	private static List<DatabaseTicket> dbTicketList = new ArrayList<DatabaseTicket>();

	public void run() throws SQLException {
		try {
			if ("1".equals(monitoraDatabase)) {
				processDatabaseMonitor();
				log.info("MONITORAMENTO DATABASE FEITO.");
			} else {
				log.info("MONITORAMENTO DATABASE DESLIGADO.");
			}
		} catch (Throwable t) {
			log.error("ERRO AO PROCESSAR - MONITORAMENTO DATABASE", t);
		}
	}

	public void processDatabaseMonitor() throws SQLException {
		dbTicketList.clear();
		log.info("Carregando estrutura de tabelas do Source:" + sourceName);
		List<Table> tableSourceList = loadTables(sourceJdbcTemplate, sourceName);
		log.info("Carregando estrutura de tabelas do Target:" + targetName);
		List<Table> tableTargetList = loadTables(targetJdbcTemplate, targetName);
		log.info("Comparando Estruturas");
		compareTable(tableSourceList, tableTargetList);
		log.info("Estruturas comparadas");
		logdataRepository.deleteByCNPJ(cnpjEmpresa);
		logdataRepository.flush();
		log.info("Deletada LOG_DTA");

		List<LOG_DTA> logDataList = new ArrayList<LOG_DTA>();
		for (DatabaseTicket dbTicket : dbTicketList) {
			LOG_DTA logData = new LOG_DTA();
			logData.setLOG_C_G_C_(cnpjEmpresa);
			logData.setLOG_TABELA(dbTicket.getTableName());
			logData.setLOG_MENSAGEM(dbTicket.getSubject());
			logDataList.add(logData);
		}
		logdataRepository.bulkSaveWithoutCheksExists(logDataList);
	}

	private void compareTable(List<Table> tableSourceList, List<Table> tableTargetList) {
		for (Table tableSource : tableSourceList) {
			int tableTargetIndex = tableTargetList.indexOf((tableSource));
			if (tableTargetIndex < 0) {
				dbTicketList.add(new DatabaseTicket(tableSource.getTableName(), "TABELA NÃO ENCONTRADA NO DATABASE DESTINO"));
			} else {
				Table tableTarget = tableTargetList.remove(tableTargetIndex);

				if (!tableTarget.getEngine().equalsIgnoreCase(tableSource.getEngine())) {
					dbTicketList.add(new DatabaseTicket(tableSource.getTableName(), String.format("ENGINE DIFERENTE NO DESTINO NO MODELO %S NO DESTINO %s ", tableSource.getEngine(),
							tableTarget.getEngine())));
				}

				if (!tableTarget.getCollation().equalsIgnoreCase(tableSource.getCollation())) {
					dbTicketList.add(new DatabaseTicket(tableSource.getTableName(), String.format("COLLATION DIFERENTE NO DESTINO NO MODELO %S NO DESTINO %s ", tableSource.getCollation(),
							tableTarget.getCollation())));
				}

				compareField(tableSource.getTableName(), tableSource.getFields(), tableTarget.getFields());
				compareIndex(tableSource.getTableName(), tableSource.getIndexes(), tableTarget.getIndexes());
				compareForeignKeys(tableSource.getTableName(), tableSource.getForeignKeys(), tableTarget.getForeignKeys());
			}
		}
//		for (Table tableTarget : tableTargetList) {
//			dbTicketList.add(new DatabaseTicket(tableTarget.getTableName(), "TABELA NÃO ENCONTRADA NO DATABASE MODELO"));
//		}
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
				if (fieldTarget.getTemCharcterSet()) {
					dbTicketList.add(new DatabaseTicket(tableName, String.format("CAMPO DO DESTINO TEM CHARACTER SET OU COLLATE: %s ", fieldTarget.getFieldName())));
				}
			}
		}
		for (Field fieldTarget : fieldTargetList) {
			if (fieldTarget.getTemCharcterSet()) {
				dbTicketList.add(new DatabaseTicket(tableName, String.format("CAMPO DO DESTINO TEM CHARACTER SET OU COLLATE: %s ", fieldTarget.getFieldName())));
			}
			dbTicketList.add(new DatabaseTicket(tableName, String.format("CAMPO NÃO ENCONTRADO NO MODELO: %s", fieldTarget.getFieldName())));
		}
	}

	private void compareIndex(String tableName, List<Index> indexSourceList, List<Index> indexTargetList) {

		List<Index> indexChecked = new ArrayList<Index>();

		for (Index indexSource : indexSourceList) {

			if (indexSource.getUnique()) {

				List<Index> matchedOnTarget = new ArrayList<Index>();
				boolean someUnique = false;
				do {
					int indexTargetIndex = indexTargetList.indexOf(indexSource);
					if (indexTargetIndex > -1) {
						Index match = indexTargetList.remove(indexTargetIndex);
						if (match.getUnique())
							someUnique = true;
						matchedOnTarget.add(match);
					}
				} while (indexTargetList.indexOf(indexSource) > -1);

				if (!matchedOnTarget.isEmpty()) {
					if (!someUnique) {
						dbTicketList.add(new DatabaseTicket(tableName, String.format("INDICE NO DESTINO DEVERIA SER UNIQUE: (%s)", indexSource.getColumnName())));
					}
				} else {
					dbTicketList.add(new DatabaseTicket(tableName, String.format("INDICE UNIQUE NÃO ENCONTRADO NO DESTINO: %s (%s)", indexSource.getIndexName(), indexSource.getColumnName())));
				}
				indexTargetList.addAll(matchedOnTarget);
				indexChecked.add(indexSource);
			} else {

				for (Index i : indexTargetList) {
					if (StringUtils.contains(i.getColumnName(), indexSource.getColumnName())) {
						indexChecked.add(indexSource);
						break;
					}
				}
			}

		}

		indexSourceList.removeAll(indexChecked);

		for (Index indexMissing : indexSourceList) {
			dbTicketList.add(new DatabaseTicket(tableName, String.format("INDICE NÃO ENCONTRADO NO DESTINO: %s (%s)", indexMissing.getIndexName(), indexMissing.getColumnName())));
		}

	}

	private void compareForeignKeys(String tableName, List<ForeignKey> foreignSourceList, List<ForeignKey> foreignTargetList) {
		for (ForeignKey foreignSource : foreignSourceList) {
			int foreignTargetIndex = foreignTargetList.indexOf(foreignSource);
			if (foreignTargetIndex < 0) {
				dbTicketList.add(new DatabaseTicket(tableName, String.format("FOREIGN KEY NÃO ENCONTRADO NO DESTINO: %s", foreignSource.getForeingKeyDescription())));
			}
		}
	}

	private List<Table> loadTables(final JdbcTemplate jdbcTemplate, final String databaseName) {
		return jdbcTemplate.query("SHOW TABLE STATUS FROM " + databaseName, new RowMapper<Table>() {
			@Override
			public Table mapRow(ResultSet rs, int rownumber) throws SQLException {
				String tableNameAsIs = rs.getString("Name");
				String tableName = StringUtils.upperCase(tableNameAsIs);
				String engine = StringUtils.upperCase(rs.getString("Engine"));
				String collation = StringUtils.upperCase(rs.getString("Collation"));

				String createTable = loadCreateTable(jdbcTemplate, tableNameAsIs);
				List<Field> fields = loadFields(jdbcTemplate, tableNameAsIs, createTable);
				List<Index> indexes = loadIndexes(jdbcTemplate, tableNameAsIs);
				List<ForeignKey> foreignKeys = loadForeignKey(createTable);

				return new Table(tableName, tableNameAsIs, engine, collation, fields, indexes, foreignKeys, createTable);
			}
		});
	}

	private List<Field> loadFields(final JdbcTemplate jdbcTemplate, final String tableName, String createTable) {
		List<Field> fieldList = jdbcTemplate.query("show columns from " + tableName, new RowMapper<Field>() {
			@Override
			public Field mapRow(ResultSet rs, int rownumber) throws SQLException {
				String fieldName = StringUtils.upperCase(rs.getString("field"));
				String type = StringUtils.upperCase(rs.getString("type"));
				Boolean nullable = "YES".equalsIgnoreCase(rs.getString("null")) ? true : false;
				String key = StringUtils.upperCase(rs.getString("key"));
				String def = StringUtils.upperCase(rs.getString("default"));
				String extra = StringUtils.upperCase(rs.getString("extra"));
				return new Field(fieldName, type, nullable, key, def, extra, false);
			}
		});

		for (Field field : fieldList) {
			field.setTemCharcterSet(procuraCharacterSet(field.getFieldName(), createTable));
		}

		return fieldList;
	}

	private Boolean procuraCharacterSet(String fieldName, String createTable) {

		for (String line : Arrays.asList(createTable.toUpperCase().split("\n"))) {
			int indexOfFIELD = line.indexOf(fieldName);
			if ((indexOfFIELD > -1) && (line.contains("CHARACTER") || line.contains("COLLATE"))) {
				return true;
			}
		}
		return false;
	}

	private List<Index> loadIndexes(final JdbcTemplate jdbcTemplate, final String tableName) {
		List<Index> indexesList = jdbcTemplate.query("show index from  " + tableName, new RowMapper<Index>() {
			@Override
			public Index mapRow(ResultSet rs, int rownumber) throws SQLException {
				String indexName = StringUtils.upperCase(rs.getString("Key_name"));
				String columnName = StringUtils.upperCase(rs.getString("Column_name"));
				Boolean unique = "0".equals(rs.getString("Non_unique")) ? true : false;

				if (!unique) {
					indexName = indexName + columnName;
				}

				return new Index(indexName, columnName, unique);
			}
		});

		Map<String, List<Index>> map = new HashMap<String, List<Index>>();

		for (Index index : indexesList) {

			if (!map.containsKey(index.getIndexName())) {
				List<Index> indexList = new ArrayList<Index>();
				indexList.add(index);
				map.put(index.getIndexName(), indexList);
			} else {
				map.get(index.getIndexName()).add(index);
			}

		}

		List<Index> indexFinalList = new ArrayList<Index>();
		for (List<Index> indexList : map.values()) {
			Index i = indexList.get(0);
			String indexString = "";
			for (Index index : indexList) {
				if (StringUtils.isEmpty(indexString)) {
					indexString = index.getColumnName();
				} else {
					indexString = indexString + "," + index.getColumnName();
				}
			}
			i.setColumnName(indexString);
			indexFinalList.add(i);
		}

		return indexFinalList;
	}

	private List<ForeignKey> loadForeignKey(final String createTable) {
		List<ForeignKey> resultList = new ArrayList<ForeignKey>();
		for (String line : Arrays.asList(createTable.split("\n"))) {

			int indexofFOREIG = line.indexOf("FOREIGN KEY");
			int length = line.length();

			if (indexofFOREIG > -1) {

				String result = StringUtils.substring(line, indexofFOREIG, length);

				if (result.endsWith(",")) {
					result = StringUtils.left(result, result.length() - 1);
				}
				resultList.add(new ForeignKey(result));
			}
		}

		return resultList;
	}

	private String loadCreateTable(final JdbcTemplate jdbcTemplate, final String tableName) {
		List<String> listCreateTable = jdbcTemplate.query("show create table  " + tableName, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String fullCreateTable = rs.getString(2);
				return fullCreateTable;
			}
		});

		return listCreateTable.get(0);
	}

}
