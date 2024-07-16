package db.com.accountservice.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpStatusCodeException;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class AccountServiceExceptionHandlerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AccountServiceExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(exceptionHandler)
                .setControllerAdvice(exceptionHandler)
                .build();
    }

    @Test
    void testHandleAllExceptions() throws Exception {
        Exception exception = new Exception("Internal Server Error");

        mockMvc.perform(get("/test-handle-all-exceptions")
                        .requestAttr("javax.servlet.error.exception", exception))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred: Internal Server Error"));
    }

    @Test
    void testHandleHttpStatusException() throws Exception {
        HttpStatusCodeException exception = mock(HttpStatusCodeException.class);
        when(exception.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(exception.getMessage()).thenReturn("Bad Request");

        mockMvc.perform(get("/test-handle-http-status-exception")
                        .requestAttr("javax.servlet.error.exception", exception))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    void testHandleAccountNotFoundException() throws Exception {
        HttpStatusCodeException exception = mock(HttpStatusCodeException.class);
        when(exception.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        when(exception.getMessage()).thenReturn("Account Not Found");

        mockMvc.perform(get("/test-handle-account-not-found-exception")
                        .requestAttr("javax.servlet.error.exception", exception))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Account Not Found"));
    }

    @Test
    void testHandleUserNotFoundException() throws Exception {
        HttpStatusCodeException exception = mock(HttpStatusCodeException.class);
        when(exception.getStatusCode()).thenReturn(HttpStatus.NOT_FOUND);
        when(exception.getMessage()).thenReturn("User Not Found");

        mockMvc.perform(get("/test-handle-user-not-found-exception")
                        .requestAttr("javax.servlet.error.exception", exception))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User Not Found"));
    }
}
