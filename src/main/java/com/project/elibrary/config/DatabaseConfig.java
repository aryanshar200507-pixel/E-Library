package com.project.elibrary.config;

import java.io.IOException;

import java.io.InputStream;
import java.util.Properties;

/**
 * Loads database and application settings from application.properties.
 *
 * This class keeps configuration values in one place so other classes do not
 * need to read the properties file themselves.
 */

public class DatabaseConfig {

	// For the final project, use application_example.properties ,so change
	// application.properties to the example one here
	private static final String configFile = "application.properties";

	// Stores all key-value pairs loaded from the properties file.
	private static final Properties PROPERTIES = new Properties();

	/*
	 * This block runs automatically when the class is first used. It loads the
	 * properties before any getter method is called.
	 */
	static {
		loadProperties();
	}

	private DatabaseConfig() {
	} // for preventing object creation

	private static void loadProperties() {
		/*
		 * getResourceAsStream() reads the properties file from the project's resources
		 * folder.
		 *
		 * try-with-resources automatically closes the InputStream.
		 */

		try (InputStream inputStream = DatabaseConfig.class.getClassLoader().getResourceAsStream(configFile)) {
			// If the file cannot be found, the application cannot
			// load the required configuration.
			if (inputStream == null) {
				throw new RuntimeException("application.properties not found.");
			}

            // Reads all key-value pairs from the properties file.
			PROPERTIES.load(inputStream);

		} catch (IOException e) {
			throw new RuntimeException("Failed to load database configuration.", e);
		}

	}

//	This helps to get Data from application.properties 
	public static String getServerUrl() {
		return PROPERTIES.getProperty("db.server.url");
	}

	public static String getDatabaseName() {
		return PROPERTIES.getProperty("db.name");
	}

	public static String getUsername() {
		return PROPERTIES.getProperty("db.username");
	}

	public static String getPassword() {
		return PROPERTIES.getProperty("db.password");
	}

	public static String getDatabaseUrl() {
		return getServerUrl() + getDatabaseName();
	}

	public static String getAdminEmail() {
		return PROPERTIES.getProperty("admin.email");
	}

	public static String getAdminPassword() {
		return PROPERTIES.getProperty("admin.password");
	}
	
	public static String getMailUsername() {
	    return PROPERTIES.getProperty("mail.username");
	}

	public static String getMailPassword() {
	    return PROPERTIES.getProperty("mail.password");
	}

	public static String getMailSmtpHost() {
	    return PROPERTIES.getProperty("mail.smtp.host");
	}

	public static String getMailSmtpPort() {
	    return PROPERTIES.getProperty("mail.smtp.port");
	}
}
