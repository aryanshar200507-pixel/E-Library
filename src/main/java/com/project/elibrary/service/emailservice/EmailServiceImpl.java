package com.project.elibrary.service.emailservice;

import java.util.Properties;

import com.project.elibrary.config.DatabaseConfig;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Handles sending emails from the E-Library application.
 *
 * This class is responsible for:
 * - Connecting to the configured mail server.
 * - Creating the email.
 * - Sending the email to the recipient.
 *
 * The reason for sending the email should be decided by
 * another service, such as the OTP or password-reset service.
 */
public class EmailServiceImpl implements EmailService {

    /**
     * Sends an email to the given recipient.
     *
     * @param recipient email address of the receiver
     * @param subject subject of the email
     * @param message content of the email
     * @return true if the email was sent successfully
     */
    @Override
    public boolean sendEmail(
            String recipient,
            String subject,
            String message) {

        /*
         * These properties tell Jakarta Mail how to connect
         * to our SMTP mail server.
         */
        Properties properties = new Properties();

        properties.put(
                "mail.smtp.host",
                DatabaseConfig.getMailSmtpHost());

        properties.put(
                "mail.smtp.port",
                DatabaseConfig.getMailSmtpPort());

        // The mail server requires username/password authentication.
        properties.put("mail.smtp.auth", "true");

        // Enables TLS to make the email connection more secure.
        properties.put(
                "mail.smtp.starttls.enable",
                "true");

        /*
         * Creates a mail session using the SMTP settings.
         *
         * Authenticator provides the username and password
         * when the mail server asks for authentication.
         */
        Session session = Session.getInstance(
                properties,
                new Authenticator() {

                    @Override
                    protected PasswordAuthentication
                    getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                DatabaseConfig.getMailUsername(),
                                DatabaseConfig.getMailPassword());
                    }
                });

        try {

            // Creates a new email message.
            Message email = new MimeMessage(session);

            // The configured mail account is used as the sender.
            email.setFrom(
                    new InternetAddress(
                            DatabaseConfig.getMailUsername()));

            // Sets the recipient of the email.
            email.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient));

            // Sets the email subject.
            email.setSubject(subject);

            // Sets the plain-text email content.
            email.setText(message);

            // Sends the email through the configured mail server.
            Transport.send(email);

            return true;

        } catch (MessagingException e) {

            /*
             * Sending the email failed.
             * The original exception is kept as the cause
             * so the actual problem can be found during debugging.
             */
            throw new RuntimeException(
                    "Failed to send email.", e);
        }
    }
}