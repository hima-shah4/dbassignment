package com.db.emailservice.service;

import com.db.emailservice.model.EmailRequest;

public interface EmailService {
	public void sendEmail(EmailRequest emailRequest);
}
