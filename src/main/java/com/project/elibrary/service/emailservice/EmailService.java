package com.project.elibrary.service.emailservice;

public interface EmailService {
	boolean sendEmail(String recipient , String subject , String message);
}
