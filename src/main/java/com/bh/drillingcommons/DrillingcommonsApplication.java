package com.bh.drillingcommons;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javax.annotation.PreDestroy;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.bh.drillingcommons.DrillingcommonsApplication;
import com.bh.drillingcommons.config.MailConfig;

@SpringBootApplication
public class DrillingcommonsApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DrillingcommonsApplication.class);
	@Autowired
	JavaMailSender emailSender;
	
	@Autowired
	MailConfig mailConfig;

	public static void main(String[] args) {
		SpringApplication.run(DrillingcommonsApplication.class, args);
	}

	@PreDestroy
	public void destroy() {
        LOGGER.info("Triggered Shutdown for Application: {}", DrillingcommonsApplication.class.getPackage().getName());
		String mailStatus = sendEmailNotify();
		LOGGER.info("mailStatus: {}",mailStatus);
	}
	
	public String sendEmailNotify() {
		String fromEmail=null;
		String toEmail =null;
		String ccEmail=null;
		String subjectLine=null;
		String[] to =null;
		String appName = null;
		String environment = null;
		try {
			LOGGER.info("from: {}", mailConfig.getEmailFrom());
			LOGGER.info("to: {}", mailConfig.getEmailTo());
			LOGGER.info("cc: {}", mailConfig.getEmailCc());
			LOGGER.info("environment: {}",mailConfig.getEnvironment());
			LOGGER.info("appName :{}",mailConfig.getAppName());
			fromEmail = mailConfig.getEmailFrom();
			toEmail   = mailConfig.getEmailTo();
			ccEmail   = mailConfig.getEmailCc();
			appName   = mailConfig.getAppName();
			appName = StringUtils.replace(WordUtils.capitalizeFully(appName, '_'), "_"," ");
			environment=mailConfig.getEnvironment();
			if(toEmail.contains(",")) {
				to =(String[]) Arrays.asList(toEmail.split(",")).toArray();	
			}else {
				to= new String[] { toEmail};
			}
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			String shutDownTimestamp = dtf.format(now);
			LOGGER.info("shutDownTimestamp: {}", shutDownTimestamp);
			String user = System.getenv("LOGNAME");
			if (user == null)
			    user = System.getProperty("user.name");
			
			String hostIP = InetAddress.getLocalHost().getHostAddress();
			
			subjectLine = appName + " Application has been taken down";
		    String emailContent =
		    "<html> " 
			+ "<head>This is an automated email notification alert triggered when the application goes down.</head>"
				+ "<body style='font-family:calibri;'><h3 style='color:#018474;'><b>DETAILS</b></h3>" 
					+ "<table border='0'>"
						+ "<tr><th align='left'><b>Application: </b></th><td>"+ appName+  "</td></tr>" 
						+ "<tr><th align='left'><b>Environment: </b></th><td>"+ environment+  "</td></tr>"
						+ "<tr><th align='left'><b>Host IP: </b></th><td>"+ hostIP+  "</td></tr>"
						+ "<tr><th align='left'><b>When :</b></th><td>"+ shutDownTimestamp+"</td> </tr>" 
						+ "<tr><th align='left'><b>Action Taken By :</b></th><td>" + user+"</td> </tr>"			
					+"</table>"
				+"</body>" 
			+ "</html>";
		    
		    MimeMessage message = emailSender.createMimeMessage();
		    
		    MimeMessageHelper helper = new MimeMessageHelper(message);
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setCc(ccEmail);
            helper.setSubject(subjectLine);
            helper.setText(emailContent, true);
            this.emailSender.send(message);
            LOGGER.info("Email Sent!!");
			return "Email Sent";
		} catch (Exception e) {
			LOGGER.info("Error in sendEmailNotify in : {}" , e);
			 return "Email not Sent";
		}
	}

}
