package com.db.emailservice.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.emailservice.model.EmailRequest;
import com.db.emailservice.service.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/email")
public class EmailController {

	private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public void sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
    		logger.debug("sending email");
         	emailService.sendEmail(emailRequest);
    }
}