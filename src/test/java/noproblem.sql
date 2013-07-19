DROP SCHEMA IF EXISTS `SourceDatabase`;
CREATE DATABASE `SourceDatabase`;
USE `SourceDatabase`;
CREATE TABLE `Tabela1` (
  `tabela1_pk` int(11) NOT NULL,	
  `tabela1_campo1` varchar(45) DEFAULT NULL,
  `tabela1_campo2` decimal(14,2) DEFAULT NULL,
  PRIMARY KEY (`tabela1_pk`),
  KEY `IDX_Tabela1` (`tabela1_campo1`,`tabela1_campo2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP SCHEMA IF EXISTS `TargetDatabase`;
CREATE DATABASE `TargetDatabase`;
USE `TargetDatabase`;
CREATE TABLE `Tabela1` (
  `tabela1_pk` int(11) NOT NULL,
  `tabela1_campo1` varchar(45) DEFAULT NULL,
  `tabela1_campo2` decimal(14,2) DEFAULT NULL,
  PRIMARY KEY (`tabela1_pk`),
  KEY `IDX_Tabela1` (`tabela1_campo1`,`tabela1_campo2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
