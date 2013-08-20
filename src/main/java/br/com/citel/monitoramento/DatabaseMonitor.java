package br.com.citel.monitoramento;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import lombok.extern.java.Log;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import br.com.citel.monitoramento.entity.LOG_DTA;
import br.com.citel.monitoramento.model.DatabaseTicket;
import br.com.citel.monitoramento.model.Field;
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

		List<LOG_DTA> logdataList = logdataRepository.findByCNPJ(cnpjEmpresa);
		logdataRepository.deleteInBatch(logdataList);

		for (DatabaseTicket dbTicket : dbTicketList) {
			LOG_DTA logData = new LOG_DTA();
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
				
				if (tableTarget.getEngine().equalsIgnoreCase(tableSource.getEngine())){
					dbTicketList.add(new DatabaseTicket(tableSource.getTableName(), String.format("COLLATION DIFERENTE NO DESTINO NO MODELO %S NO DESTINO %s ", tableSource.getEngine(), tableTarget.getEngine())));
				}
				
				if (tableTarget.getCollation().equalsIgnoreCase(tableSource.getCollation())){
					dbTicketList.add(new DatabaseTicket(tableSource.getTableName(), String.format("COLLATION DIFERENTE NO DESTINO NO MODELO %S NO DESTINO %s ", tableSource.getEngine(), tableTarget.getEngine())));
				}
				
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
		
		List<Index> indexChecked = new ArrayList<Index>();
		
		for (Index indexSource : indexSourceList) {
			
			if ( indexSource.getUnique() ){
				int indexTargetIndex = indexTargetList.indexOf(indexSource);
				if (indexTargetIndex > -1){
					Index indexTarget = indexTargetList.get(indexTargetIndex);
					if (indexSource.getUnique() != indexTarget.getUnique()){
						dbTicketList.add(new DatabaseTicket(tableName, String.format("INDICE NO DESTINO DEVERIA SER UNIQUE: %s (%s)", indexTarget.getIndexName(), indexTarget.getColumnName())));
					}
				}else{
					dbTicketList.add(new DatabaseTicket(tableName, String.format("INDICE UNIQUE NÃO ENCONTRADO NO DESTINO: %s (%s)", indexSource.getIndexName(), indexSource.getColumnName())));
				}
				indexChecked.add(indexSource);
			}else{
				
				int indexTargetIndex = indexTargetList.indexOf(indexSource);
				
				if (indexTargetIndex > -1){
					indexChecked.add(indexSource);
				}
				
				
				for (Index i : indexTargetList){
					
					if (StringUtils.contains(i.getColumnName(),indexSource.getColumnName())){
						indexChecked.add(indexSource);
					}
					
				}
				
			}
			
		}
		
		indexSourceList.removeAll(indexChecked);
		
		for (Index indexMissing: indexSourceList){
			dbTicketList.add(new DatabaseTicket(tableName, String.format("INDICE NÃO ENCONTRADO NO DESTINO: %s (%s)", indexMissing.getIndexName(), indexMissing.getColumnName())));
		}
			
	}

	private List<Table> loadTables(final JdbcTemplate jdbcTemplate) {
		return jdbcTemplate.query("SHOW TABLE STATUS FROM AUTCOM", new RowMapper<Table>() {
			@Override
			public Table mapRow(ResultSet rs, int rownumber) throws SQLException {
				String tableName = rs.getString("Name");
				String engine = StringUtils.upperCase(rs.getString("Engine"));
				String collation = StringUtils.upperCase(rs.getString("Collation"));
				return new Table(tableName, engine, collation, loadFields(jdbcTemplate, tableName), loadIndexes(jdbcTemplate, tableName));
			}
		});
	}

	private List<Field> loadFields(final JdbcTemplate jdbcTemplate, final String tableName) {
		return jdbcTemplate.query("show columns from " + tableName, new RowMapper<Field>() {
			@Override
			public Field mapRow(ResultSet rs, int rownumber) throws SQLException {
				String fieldName = StringUtils.upperCase(rs.getString("field"));
				String type = StringUtils.upperCase(rs.getString("type"));
				Boolean nullable =  "YES".equalsIgnoreCase( rs.getString("null") ) ? true : false;
				String key = StringUtils.upperCase(rs.getString("key"));
				String def = StringUtils.upperCase(rs.getString("default"));
				String extra = StringUtils.upperCase(rs.getString("extra"));
				return new Field(fieldName, type, nullable, key, def, extra);
			}
		});
	}

	private List<Index> loadIndexes(final JdbcTemplate jdbcTemplate, final String tableName) {
		List<Index> indexesList = jdbcTemplate.query("show index from  " + tableName, new RowMapper<Index>() {
			@Override
			public Index mapRow(ResultSet rs, int rownumber) throws SQLException {
				String indexName = StringUtils.upperCase(rs.getString("Key_name"));
				String columnName = StringUtils.upperCase(rs.getString("Column_name"));
				Boolean unique = "0".equals( rs.getString("Non_unique") ) ? true : false ; 
				return new Index(indexName, columnName, unique);
			}
		});
		
		Map<String,List<Index>> map = new HashMap<String, List<Index>>(); 
		
		for (Index index : indexesList){
			if (!map.containsKey(index.getIndexName())){
				List<Index> indexList = new ArrayList<Index>();
				indexList.add(index);
				map.put(index.getIndexName(), indexList);
			}else{
				map.get(index.getIndexName()).add(index);
			}
		}
		
		List<Index> indexFinalList = new ArrayList<Index>();
		for ( List<Index> indexList : map.values() ){
			Index i = indexList.get(0);
			String indexString = ""; 
			for (Index index : indexList){
				if (StringUtils.isEmpty(indexString)){
					indexString = index.getColumnName();
				}else{
					indexString = indexString + "," +  index.getColumnName();
				}
			}
			i.setColumnName(indexString);
			indexFinalList.add(i);
		}
		
		return indexFinalList;
	}

}
