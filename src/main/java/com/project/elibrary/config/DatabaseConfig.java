package com.project.elibrary.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
	
//	change application.properties to application_example.properties 
	private static final String configFile = "application.properties";
	
	private static final Properties PROPERTIES = new Properties();
	
	static {
		loadProperties();
	}
	
	private DatabaseConfig() {} // for preventing object creation 
	
	private static void loadProperties() {
		
		try(InputStream inputStream = DatabaseConfig.class.getClassLoader().getResourceAsStream(configFile) ){
			if(inputStream == null) {
				throw new RuntimeException("application.properties not found.");
			}
			PROPERTIES.load(inputStream);
			
		}catch (IOException e) {
			throw new RuntimeException("Failed to load database configuration.",e);
		}
			
			
	}
	
	public static  String getServerUrl(){
		return PROPERTIES.getProperty("db.server.url");
	}
	public static  String getDatabaseName(){
		return PROPERTIES.getProperty("db.name");
	}
	public static  String getUsername(){
		return PROPERTIES.getProperty("db.username");
	}
	public static  String getPassword(){
		return PROPERTIES.getProperty("db.password");
	}
	public static  String getDatabaseUrl(){
		return getServerUrl()+getDatabaseName();
	}
}
