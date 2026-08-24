package com.project.elibrary.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private DatabaseConnection() {
	} // prevent object creation of db-connection

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DatabaseConfig.getDatabaseUrl(), DatabaseConfig.getUsername(),
				DatabaseConfig.getPassword()

		);

	}
}
