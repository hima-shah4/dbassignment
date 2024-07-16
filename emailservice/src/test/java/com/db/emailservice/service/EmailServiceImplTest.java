package com.db.emailservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.db.emailservice.model.EmailRequest;

import java.util.concurrent.Future;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Value("${spring.mail.from}")
    private String emailFrom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(emailService, "emailFrom", emailFrom);
    }

    @Test
    void testSendEmail() throws Exception {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmailTo("test@example.com");
        emailRequest.setSubject("Test Subject");
        emailRequest.setMessage("Test Message");

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        Future<Void> future = AsyncResult.forValue(null);
        ReflectionTestUtils.setField(emailService, "mailSender", mailSender);
        
        emailService.sendEmail(emailRequest);
        future.get(); // Wait for the async method to complete

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}