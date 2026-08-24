package com.project.elibrary.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationStartup implements ServletContextListener{
	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("Starting E-Library application...");// remove in future 
		DatabaseIniti.initialize();
		System.out.println("E-Library application start successfully");//remove in future 
	}
	
	@Override 
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("E-Library application stopped."); //remove in future 
	}
}
