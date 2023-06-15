package com.bh.drillingcommons.config;

import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bh.drillingcommons.exceptions.BadRequestException;
import com.bh.drillingcommons.exceptions.SystemException;
import com.bh.drillingcommons.models.Email;

@Component
public class EmailUtil {

	@Autowired
	private Environment environment;

	@Autowired
	public JavaMailSender emailSender;

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);

	private EmailUtil() {
	}

	/**
	 * Get {@link String} object from {@link List<String>}
	 * 
	 * @param feature {@link List<String>}
	 * 
	 * @return Returns a {@link String}
	 */
	public Properties getSmtpProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", environment.getProperty("mail.smtp.host"));
		properties.put("mail.smtp.port", environment.getProperty("mail.smtp.port"));
		return properties;
	}

	/**
	 * constructAndSendEmail using {@link Email} object
	 * 
	 * @param feature {@link Email}
	 * 
	 * @return NIL
	 */
	public void constructAndSendEmail(Email email, String[] toEmail, boolean isHtml) {
		validateEmailContent(email, toEmail);
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setFrom(new InternetAddress(email.getFrom()));
			if (!StringUtils.isEmpty(email.getTo()))
				helper.setTo(email.getTo());
			else
				helper.setTo(toEmail);
			if (email.getBcc() != null && email.getBcc().length > 0)
				helper.setBcc(email.getBcc());
			if (email.getCc() != null && email.getCc().length > 0)
				helper.setCc(email.getCc());
			helper.setSubject(email.getSubject());
			helper.setSentDate(new Date());
			helper.setText(email.getBody(), isHtml);
			this.emailSender.send(message);
			LOGGER.info("Email sent successfully !! {}");
		} catch (MessagingException e) {
			LOGGER.error("Exception in constructAndSendEmail, Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * Validate Email Object
	 * 
	 * @param email
	 */
	private void validateEmailContent(Email email, String[] emailToList) {
		if (email == null || (StringUtils.isEmpty(email.getTo()) && emailToList == null)
				|| StringUtils.isEmpty(email.getFrom()))
			throw new BadRequestException("Invalid From/To Email Address");
	}
}
