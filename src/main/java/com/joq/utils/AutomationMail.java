package com.joq.utils;

import java.io.File;
import java.io.FileFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.joq.exception.AutomationException;
import com.joq.keywords.DataHandler;

public class AutomationMail {

	DataHandler dataHandler = new DataHandler();

	/**
	 * Method to send the execution report to the recipients list mentioned in the
	 * email_config file
	 * 
	 * @author sanoj.swaminathan
	 * @since 03-05-2023
	 * @throws AutomationException
	 */
	public void sendMailReport() throws AutomationException {
		String mailHost = "", subject = "", message = "", maillist = "";

		mailHost = dataHandler.getProperty(AutomationConstants.EMAIL_CONFIG, "mailHost");
		subject = dataHandler.getProperty(AutomationConstants.EMAIL_CONFIG, "subject");
		message = dataHandler.getProperty(AutomationConstants.EMAIL_CONFIG, "message");
		maillist = dataHandler.getProperty(AutomationConstants.EMAIL_CONFIG, "maillist");

		Properties properties = new Properties();
		if (mailHost.equalsIgnoreCase("gmail")) {
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.port", "587");
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
		}
		if (mailHost.equalsIgnoreCase("outlook")) {
			properties.put("mail.smtp.host", "outlook.office365.com");
			properties.put("mail.smtp.port", "587");
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		}

		String username = dataHandler.getProperty(AutomationConstants.EMAIL_CONFIG, "username");
		String password = dataHandler.getProperty(AutomationConstants.EMAIL_CONFIG, "password");

		properties.put("mail.user", username);
		properties.put("mail.password", password);
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
		Session session = Session.getInstance(properties, auth);
		DateFormat dff = new SimpleDateFormat("EEE MMM dd, yyyy HH:mm:ss z");

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(username));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(maillist));
			msg.setSubject(subject + " – " + dff.format(new Date()).toString());
			msg.setSentDate(new Date());

			// creates message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(message, "text/html");

			// creates multi-part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// adds only the latest Report file in attachments
			MimeBodyPart attachPart = new MimeBodyPart();
			attachPart.attachFile(
					lastFileModified(System.getProperty("user.dir") + "\\Reports\\Security_Execution_Reports\\"));
			multipart.addBodyPart(attachPart);

			// sets the multi-part as e-mail’s content
			msg.setContent(multipart);
			// sends the e-mail
			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to get the latest modified report
	 * 
	 * @author sanoj.swaminathan
	 * @since 03-05-2023
	 * @param filePath
	 * @throws AutomationException
	 */
	private File lastFileModified(String filePath) throws AutomationException {
		File choice = null;
		try {
			File fl = new File(filePath);
			File[] files = fl.listFiles(new FileFilter() {

				public boolean accept(File file) {
					return file.isFile();
				}
			});
			long lastMod = Long.MIN_VALUE;
			for (File file : files) {
				if (file.lastModified() > lastMod) {
					choice = file;
					lastMod = file.lastModified();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return choice;
	}
}
