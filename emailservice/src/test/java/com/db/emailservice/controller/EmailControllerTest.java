package com.db.emailservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.db.emailservice.model.EmailRequest;
import com.db.emailservice.service.EmailService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmailController.class)
class EmailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
    }

    @Test
    void testSendEmail() throws Exception {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmailTo("test@gmail.com");
        emailRequest.setSubject("Test Subject");
        emailRequest.setMessage("Test Message");

        doNothing().when(emailService).sendEmail(any(EmailRequest.class));

        mockMvc.perform(post("/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"emailTo\":\"test@gmail.com\", \"subject\":\"Test Subject\", \"message\":\"Test Message\"}"))
                .andExpect(status().isOk());
    }
}