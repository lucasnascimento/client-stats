package br.com.citel.monitoramento;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Setter;
import lombok.extern.java.Log;

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
				compareField(tableSource.getTableName(), tableSource.getFields(), tableTarget.getFields());
				compareIndex(tableSource.getTableName(), tableSource.getIndexes(), tableTarget.getIndexes());
				compareForeignKeys(tableSource.getTableName(), tableSource.getForeignKeys(), tableTarget.getForeignKeys());
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
	
	private void compareForeignKeys(String tableName, List<ForeignKey> foreignSourceList, List<ForeignKey> foreignTargetList) {
		for (ForeignKey foreignSource : foreignSourceList) {
			int foreignTargetIndex = foreignTargetList.indexOf(foreignSource);
			if (foreignTargetIndex < 0) {
				dbTicketList.add(new DatabaseTicket(tableName, String.format("FOREIGN KEY NÃO ENCONTRADO NO DESTINO: %s", foreignSource.getForeingKeyDescription())));
			} 
		}
	}

	private List<Table> loadTables(final JdbcTemplate jdbcTemplate) {
		return jdbcTemplate.query("show tables", new RowMapper<Table>() {
			@Override
			public Table mapRow(ResultSet rs, int rownumber) throws SQLException {
				String tableName = rs.getString(1);
				return new Table(tableName, loadFields(jdbcTemplate, tableName), loadIndexes(jdbcTemplate, tableName), loadForeignKey(jdbcTemplate, tableName));
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

	private List<ForeignKey> loadForeignKey(final JdbcTemplate jdbcTemplate, final String tableName) {

		List<String> listCreateTable = jdbcTemplate.query("show create table  " + tableName, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String fullCreateTable = rs.getString(2);
				return fullCreateTable;
			}
		});

		String descriptionCreateTable = listCreateTable.get(1);

		return loadForeignKeyFromDescription(descriptionCreateTable);
	}

	private List<ForeignKey> loadForeignKeyFromDescription(String descriptionCreateTable) {

		List<ForeignKey> resultList = new ArrayList<ForeignKey>();
		for (String line : Arrays.asList(descriptionCreateTable.split("/n"))) {

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

	// CREATE TABLE `CADREC` (
	// `CTR_NUMDUP` varchar(6) NOT NULL default '',
	// `CTR_PARCEL` char(1) NOT NULL default '',
	// `CTR_SEQBAI` int(11) NOT NULL default '0',
	// `CTR_ESPDOC` char(2) NOT NULL default '',
	// `CTR_CODCLI` varchar(8) NOT NULL default '',
	// `CTR_CODEMP` char(3) NOT NULL default '',
	// `CTR_CODVEN` char(3) default NULL,
	// `CTR_CODDIG` char(3) default NULL,
	// `CTR_CODPOR` varchar(5) default NULL,
	// `CTR_DTACAD` date NOT NULL default '0000-00-00',
	// `CTR_DTAVEN` date NOT NULL default '0000-00-00',
	// `CTR_VALDUP` decimal(12,2) default NULL,
	// `CTR_VALORI` decimal(12,2) default NULL,
	// `CTR_DESCOB` decimal(12,2) default NULL,
	// `CTR_DESCON` char(1) default 'N',
	// `CTR_HIST01` text,
	// `CTR_HIST02` varchar(50) default NULL,
	// `CTR_PORBAI` varchar(5) default NULL,
	// `CTR_HISBAI` varchar(50) default NULL,
	// `CTR_CTABAI` varchar(7) default NULL,
	// `CTR_DOCBAI` varchar(13) default NULL,
	// `CTR_OPEBAI` char(3) default NULL,
	// `CTR_DTAREC` date default NULL,
	// `CTR_VALREC` decimal(12,2) default NULL,
	// `CTR_VALJUR` decimal(12,2) default NULL,
	// `CTR_VALDES` decimal(12,2) default NULL,
	// `CTR_RESCAI` tinyint(1) default NULL,
	// `CTR_NOSNUM` varchar(12) default NULL,
	// `CTR_DTABOL` date default NULL,
	// `CTR_BCOBOL` char(3) default NULL,
	// `CTR_BCOENV` char(3) default NULL,
	// `CTR_DTAENV` date default NULL,
	// `AUTOINCREM` varchar(20) NOT NULL default '',
	// `CTR_OBSAG1` varchar(80) default NULL,
	// `CTR_OBSAG2` varchar(80) default NULL,
	// `CTR_OBSAG3` varchar(80) default NULL,
	// `CTR_DTABAI` date default NULL,
	// `CTR_CODEVE` varchar(5) default NULL,
	// `CTR_CODVEL` varchar(20) default NULL,
	// `CTR_DINHEI` decimal(12,2) default '0.00',
	// `CTR_CHQAVI` decimal(12,2) default '0.00',
	// `CTR_CHQPRE` decimal(12,2) default '0.00',
	// `CTR_CARTAO` decimal(12,2) default '0.00',
	// `CTR_SEQMOD` int(11) default NULL,
	// `CTR_TIPFOR` varchar(10) default NULL,
	// `CTR_CODCND` char(3) default NULL,
	// `CTR_CONAPR` tinyint(1) default NULL,
	// `CTR_RECDIN` char(1) default 'N',
	// `CTR_RECCHE` char(1) default 'N',
	// `CTR_RECPRE` char(1) default 'N',
	// `CTR_RECCAR` char(1) default 'N',
	// `CTR_CODCTA` varchar(7) default NULL,
	// `CTR_CTASER` varchar(7) default NULL,
	// `CTR_SERVAL` decimal(12,2) default NULL,
	// `CTR_ORDPAG` decimal(12,2) default '0.00',
	// `CTR_RECORD` char(1) default 'N',
	// `CTR_VALCOM` decimal(12,2) default NULL,
	// `CTR_FATCOM` decimal(12,8) default NULL,
	// `CTR_SEQCOM` varchar(20) default NULL,
	// `CTR_DESVEN` decimal(5,2) default '0.00',
	// `CTR_EMPBAI` char(3) default NULL,
	// `CTR_DTASER` date default NULL,
	// `CTR_VALIPI` decimal(12,2) default '0.00',
	// `CTR_VALRET` decimal(12,2) default '0.00',
	// `CTR_DOCDIV` varchar(6) default NULL,
	// `CTR_ESPDIV` char(2) default NULL,
	// `CTR_EMPDIV` char(3) default NULL,
	// `CTR_CLIDIV` varchar(8) default NULL,
	// `CTR_JURSUJ` decimal(12,2) default '0.00',
	// `CTR_JURANT` decimal(12,2) default '0.00',
	// `CTR_DESSUJ` decimal(12,2) default '0.00',
	// `CTR_DESANT` decimal(12,2) default '0.00',
	// `CTR_SEQCON` int(11) default '0',
	// `CTR_QTDDAV` int(3) default '0',
	// `CTR_PERDPO` decimal(12,2) default '0.00',
	// `CTR_CODREP` char(3) default NULL,
	// `CTR_COMREP` decimal(5,2) default NULL,
	// `CTR_FATREP` decimal(12,8) default NULL,
	// `CTR_VALANT` decimal(12,2) default NULL,
	// `CTR_ORIF11` char(1) default 'N',
	// `CTR_VALFIN` decimal(12,2) default NULL,
	// `CTR_RECFIN` char(1) default NULL,
	// `CTR_EMP_PD` char(3) default NULL,
	// `CTR_NOMFRM` varchar(70) default NULL,
	// `CTR_VRTRDV` decimal(12,2) default NULL,
	// `CTR_BARCON` varchar(13) default NULL,
	// `CTR_COMDIV` decimal(12,2) default NULL,
	// `CTR_FATDIV` decimal(12,2) default NULL,
	// `CTR_CODDIV` varchar(8) default NULL,
	// `CTR_PROTES` varchar(12) default NULL,
	// `CTR_CTREME` varchar(10) default NULL,
	// `CTR_DTAPFI` date default NULL,
	// `CTR_MOTBAI` char(2) default NULL,
	// `CTR_VALFRE` decimal(12,2) default '0.00',
	// `CTR_CTPCOM` varchar(9) default NULL,
	// `CTR_VALACE` decimal(12,2) default '0.00',
	// PRIMARY KEY (`AUTOINCREM`),
	// UNIQUE KEY `UK_CADREC_01`
	// (`CTR_NUMDUP`,`CTR_PARCEL`,`CTR_SEQBAI`,`CTR_ESPDOC`,`CTR_CODCLI`,`CTR_CODEMP`),
	// KEY `IDX_CADREC_01` (`CTR_CODPOR`),
	// KEY `IDX_CADREC_02` (`CTR_CODVEN`),
	// KEY `IDX_CADREC_03` (`CTR_CODEVE`),
	// KEY `IDX_CADREC_04` (`CTR_DTACAD`),
	// KEY `IDX_CADREC_05` (`CTR_CODCTA`),
	// KEY `IDX_CADREC_06` (`CTR_CODCLI`,`CTR_DTAREC`),
	// KEY `IDX_CADREC_07` (`CTR_DTAREC`),
	// KEY `IDX_CADREC_08` (`CTR_CODCLI`,`CTR_DTAVEN`),
	// KEY `IDX_CADREC_09` (`CTR_CODCLI`,`CTR_DTACAD`),
	// KEY `IDX_CADREC_10` (`CTR_DTAVEN`),
	// KEY `IDX_CADREC_11` (`AUTOINCREM`),
	// KEY `IDX_CADREC_12`
	// (`CTR_ESPDOC`,`CTR_DOCDIV`,`CTR_ESPDIV`,`CTR_CLIDIV`,`CTR_EMPDIV`),
	// KEY `IDX_CADREC_13` (`CTR_SEQCON`,`CTR_EMPBAI`),
	// KEY `IDX_CADREC_14`
	// (`CTR_NUMDUP`,`CTR_ESPDOC`,`CTR_CODEMP`,`CTR_CODCLI`),
	// KEY `IDX_CADREC_15` (`CTR_NOSNUM`),
	// KEY `IDX_CADREC_16` (`CTR_CODDIG`),
	// KEY `IDX_CADREC_17` (`CTR_EMPBAI`),
	// KEY `IDX_CADREC_18` (`CTR_CODREP`),
	// KEY `IDX_CADREC_19` (`CTR_CODEMP`,`CTR_CTPCOM`),
	// CONSTRAINT `FK_CADREC_01` FOREIGN KEY (`CTR_CODDIG`) REFERENCES `CADOPE`
	// (`OPE_CODOPE`),
	// CONSTRAINT `FK_CADREC_02` FOREIGN KEY (`CTR_EMPBAI`) REFERENCES `CADEMP`
	// (`EMP_CODEMP`),
	// CONSTRAINT `FK_CADREC_03` FOREIGN KEY (`CTR_CODREP`) REFERENCES `CADOPE`
	// (`OPE_CODOPE`),
	// CONSTRAINT `FK_CADREC_04` FOREIGN KEY (`CTR_CODEVE`) REFERENCES `CADEVE`
	// (`EVE_CODEVE`),
	// CONSTRAINT `FK_CADREC_05` FOREIGN KEY (`CTR_CODPOR`) REFERENCES `CADPOR`
	// (`POR_CODPOR`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	// CONSTRAINT `FK_CADREC_06` FOREIGN KEY (`CTR_CODCLI`) REFERENCES `CADCLI`
	// (`CLI_CODCLI`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	// CONSTRAINT `FK_CADREC_07` FOREIGN KEY (`CTR_CODVEN`) REFERENCES `CADOPE`
	// (`OPE_CODOPE`) ON DELETE NO ACTION ON UPDATE NO ACTION,
	// CONSTRAINT `FK_CADREC_08` FOREIGN KEY (`CTR_CODCTA`) REFERENCES `CADCTA`
	// (`CTA_CODCTA`)
	// ) ENGINE=InnoDB DEFAULT CHARSET=latin1
}
