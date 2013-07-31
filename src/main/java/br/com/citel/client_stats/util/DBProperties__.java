package br.com.citel.client_stats.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DBProperties__ {

	static final String CONFIG_FILE_NAME = "config.properties";

	private static DBProperties__ properties = null;

	static final Properties PROPS = new Properties();

	private String sourceDatebase;
	private String sourceHost;
	private String sourcePort;
	private String sourceUser;
	private String sourcePassword;
	private String targetDatebase;
	private String targetHost;
	private String targetPort;
	private String targetUser;
	private String targetPassword;

	public DBProperties__() {
		try {
			InputStream is = ClassLoader.getSystemResourceAsStream(CONFIG_FILE_NAME);
			if (is != null)
				PROPS.load(is);
			else
				PROPS.load(new FileInputStream(new File(CONFIG_FILE_NAME)));
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public synchronized static DBProperties__ getInstance() {
		if (properties == null) {
			properties = new DBProperties__();
		}
		return properties;
	}

	public String getSourceDatebase() {
		if (sourceDatebase == null)
			sourceDatebase = PROPS.getProperty("source.Database");
		return sourceDatebase;
	}

	public String getSourceHost() {
		if (sourceHost == null)
			sourceHost = PROPS.getProperty("source.Host");
		return sourceHost;
	}

	public String getSourcePort() {
		if (sourcePort == null)
			sourcePort = PROPS.getProperty("source.Port");
		return sourcePort;
	}

	public String getSourceUser() {
		if (sourceUser == null)
			sourceUser = PROPS.getProperty("source.User");
		return sourceUser;
	}

	public String getSourcePassword() {
		if (sourcePassword == null)
			sourcePassword = PROPS.getProperty("source.Password");
		return sourcePassword;
	}

	public String getTargetDatebase() {
		if (targetDatebase == null)
			targetDatebase = PROPS.getProperty("target.Database");
		return targetDatebase;
	}

	public String getTargetHost() {
		if (targetHost == null)
			targetHost = PROPS.getProperty("target.Host");
		return targetHost;
	}

	public String getTargetPort() {
		if (targetPort == null)
			targetPort = PROPS.getProperty("target.Port");
		return targetPort;
	}

	public String getTargetUser() {
		if (targetUser == null)
			targetUser = PROPS.getProperty("target.User");
		return targetUser;
	}

	public String getTargetPassword() {
		if (targetPassword == null)
			targetPassword = PROPS.getProperty("target.Password");
		return targetPassword;
	}

}
