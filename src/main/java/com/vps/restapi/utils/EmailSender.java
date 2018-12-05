package com.vps.restapi.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vps.restapi.model.User;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class EmailSender {
	private static final Logger LOG = LoggerFactory.getLogger(EmailSender.class);

	static String userName = "spring.rest.email.service@gmail.com";
	static String password = "Qwerty!123";
	static String host = "smtp.gmail.com";
	static int port = 465;
	static String fromAddress = "noreply@service.com";

	@Autowired
	private static Configuration getFreeMarkerConfiguration;

	public static void newAccountEmail(User userNew) throws EmailException, TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException, TemplateException {
		// zapytac o exception
		String subject = "Hello " + userNew.getFirstName();
		String message = "Hello " + userNew.getFirstName() + " " + userNew.getLastName();
		String confirmation = "<br>Yr conf link: http://localhost:8080/user/confirm/" + userNew.getToken();
		StringBuffer msg = new StringBuffer();
		msg.append("<html><body>");
		msg.append("<br>");
		msg.append(message + confirmation);
		msg.append("</br>");
		msg.append("</body></html>");

		// Template t = getFreeMarkerConfiguration.getTemplate("emial-template.ftl");
		// String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, userNew);
		sendEmail(initHtmlEmail(), userNew.getEmail(), subject, msg.toString(), Collections.emptyMap());

	}

	public static void editAccountEmail(Optional<User> userFromDatabase, User userNew) throws Exception {

		User userOld = userFromDatabase.get();
		String subject = "Hello " + userOld.getFirstName();
		String message = "<h1>Hello " + userOld.getFirstName() + " " + userOld.getLastName()
				+ " your account was edited</h1>";

		String emailChange = "Previus email: " + userOld.getEmail() + "<br> New email: " + userNew.getEmail();
		String firstNameChange = "Previus name: " + userOld.getFirstName() + "<br> New name: " + userNew.getFirstName();
		String lastNameChange = "Previous last name: " + userOld.getLastName() + "<br> New last name: "
				+ userNew.getLastName();
		String passwordChange = "Previous password: " + userOld.getPassword() + "<br> New password: "
				+ userNew.getPassword();

		StringBuffer msg = new StringBuffer();
		msg.append("<html><body>");
		msg.append("<img src=cid:doors.jpg>");
		msg.append("<br>");
		msg.append(message);
		msg.append("</br>");
		msg.append("<h3>" + emailChange + "</h3>");
		msg.append("<h3>" + firstNameChange + "</h3>");
		msg.append("<h3>" + lastNameChange + "</h3>");
		msg.append("<h3>" + passwordChange + "</h3>");

		msg.append("</body></html>");

		Map<String, File> imagesToEmbed = new HashMap<>();
		File img = new File("C://Users/filip/Downloads/doors.jpg");
		imagesToEmbed.put(img.getName(), img);
		sendEmail(initHtmlEmail(), userNew.getEmail(), subject, msg.toString(), imagesToEmbed);

	}

	public static void deleteAccountEmail(User userDeleted) throws EmailException {
		String subject = "Hello " + userDeleted.getFirstName();
		String message = "<h1>Hello</h1><br><h1> " + userDeleted.getFirstName() + " " + userDeleted.getLastName()
				+ " your account was deleted</h1>";

		StringBuffer msg = new StringBuffer();
		msg.append("<html><body>");
		msg.append("<img src=cid:doors.jpg>");
		msg.append("<br>");
		msg.append(message);
		msg.append("</br>");
		msg.append("</body></html>");

		Map<String, File> imagesToEmbed = new HashMap<>();
		File img = new File("C://Users/filip/Downloads/doors.jpg");
		imagesToEmbed.put(img.getName(), img);
		sendEmail(initHtmlEmail(), userDeleted.getEmail(), subject, msg.toString(), imagesToEmbed);
	}

	public static void confirmationAccountEmail(User userConfirmed) throws EmailException {
		String subject = "Account Confirmation";
		String message = "<h1>" + userConfirmed.getFirstName() + "!" + "</h1>"
				+ "<h1>Your account has been verified</h1>";

		StringBuffer msg = new StringBuffer();
		msg.append("<html><body>");
		msg.append("<br>");
		msg.append(message);
		msg.append("</br>");
		msg.append("</body></html>");

		sendEmail(initHtmlEmail(), userConfirmed.getEmail(), subject, msg.toString(), Collections.emptyMap());
	}

	public static void changePassword(User userPassword) throws EmailException {
		String subject = "Change password";
		String message = "<h1>" + userPassword.getFirstName() + "!" + "</h1>" + "<h1>click here to change password</h1>"
				+ "http://localhost:8080/user/password" + userPassword.getToken();
		// link do angulara z przeniesiona zmienna email uzytkownika
		StringBuffer msg = new StringBuffer();
		msg.append("<html><body>");
		msg.append("<br>");
		msg.append(message);
		msg.append("</br>");
		msg.append("</body></html>");

		sendEmail(initHtmlEmail(), userPassword.getEmail(), subject, msg.toString(), Collections.emptyMap());
	}

	private static void sendEmail(HtmlEmail htmlEmail, String toAddress, String subject, String message,
			Map<String, File> imagesToEmbed) {
		try {
			htmlEmail.setSubject(subject);
			htmlEmail.setHtmlMsg(message);
			htmlEmail.addTo(toAddress);
			for (Entry<String, File> imagesToEmbedEntry : imagesToEmbed.entrySet()) {
				htmlEmail.embed(imagesToEmbedEntry.getValue(), imagesToEmbedEntry.getKey());
			}
			// java collections/map
			htmlEmail.send();
			LOG.info("email send to :" + toAddress);
		} catch (Exception ex) {
			LOG.error("Unable to send email : " + ex);
		}
	}

	// wyciagnac emaila do pliku, zrobic szablon freemaker dla nazw uzytkownika itp,
	// uzytkownik nie usuwany ale zmiana aktywnosci
	// mail z aktywacja konta
	// metoda w api ktora przyjmie token zweryfikuje, czy ma uzytkownik go
	// przypisanego i jezeli tak to najs

	private static HtmlEmail initHtmlEmail() throws EmailException {
		HtmlEmail he = new HtmlEmail();
		he.setHostName(host);
		he.setSmtpPort(port);
		he.setAuthentication(userName, password);
		he.setSSLOnConnect(true);
		he.setFrom(fromAddress);
		return he;
	}

}