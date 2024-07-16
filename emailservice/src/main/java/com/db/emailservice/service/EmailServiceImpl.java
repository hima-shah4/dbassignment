package com.db.emailservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.db.emailservice.model.EmailRequest;
import com.db.emailservice.model.InvalidMessageException;

@Service
public class EmailServiceImpl implements EmailService{
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.from}")
	private String emailFrom;
	
	private final String SEPERATOR =",";
	
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

//	@Async
	@Override
	public void sendEmail(EmailRequest emailRequest) {
		 System.out.println("Email request = "+ emailRequest);
		 logger.debug("emailRequest = {}",emailRequest);
		 SimpleMailMessage message = new SimpleMailMessage();
	     message.setFrom(emailFrom);
	     message.setTo(emailRequest.getEmailTo());
	     message.setSubject(emailRequest.getSubject());
	     message.setText(emailRequest.getMessage());
         mailSender.send(message);
         logger.debug("Sending email");
	}
	
	@JmsListener(destination = "message-queue")
	public void receiveMessage(String message) {
		logger.debug("Message received :{}",message);
		if(message!=null && !message.isEmpty() && message.split(SEPERATOR).length==3) {
			String [] msgArr = message.split(SEPERATOR);
			EmailRequest emailRequest = new EmailRequest(msgArr[0], msgArr[1],msgArr[2]);
			this.sendEmail(emailRequest);
		}else {
			throw new InvalidMessageException("Invalid message received");
		}
	}
}
