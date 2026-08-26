package com.project.elibrary.config;

import jakarta.servlet.ServletContextEvent;

import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;


/**
 * Listens for the start and stop of the E-Library web application.
 *
 * When the application starts, this class initializes the database
 * and required tables before the application begins handling requests.
 */

@WebListener
public class ApplicationStartup implements ServletContextListener{
	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("Starting E-Library application...");// remove in future 
		DatabaseIniti.initialize();
		System.out.println("E-Library application start successfully");//remove in future 
	}
	 /**
     * Runs when the web application is stopped or undeployed.
     *
     * Currently there is no cleanup required here.
     * This method is kept because ServletContextListener provides
     * lifecycle callbacks for both application startup and shutdown.
     */
	@Override 
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("E-Library application stopped."); //remove in future 
	}
}
